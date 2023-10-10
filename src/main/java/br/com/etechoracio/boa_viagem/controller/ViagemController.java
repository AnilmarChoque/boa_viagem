package br.com.etechoracio.boa_viagem.controller;

import br.com.etechoracio.boa_viagem.entity.Gasto;
import br.com.etechoracio.boa_viagem.entity.Viagem;
import br.com.etechoracio.boa_viagem.repository.GastoRepository;
import br.com.etechoracio.boa_viagem.repository.ViagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/viagens")
public class ViagemController {
	
	@Autowired
	private ViagemRepository repository;
	@Autowired
	private GastoRepository gastoRepository;
	
	@GetMapping
	public List<Viagem> listarTodos(@RequestParam(required = false) String destino) {
		if (destino == null) {
			return repository.findAll();
		}
		return repository.findByDestinoIgnoreCase(destino);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Viagem> buscarPorId(@PathVariable Long id) {
		Optional<Viagem> existe = repository.findById(id);
		return existe.isPresent() ? ResponseEntity.ok(existe.get())
								  : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<Viagem> inserir(@RequestBody Viagem obj) {
		Viagem salva = repository.save(obj);
		return ResponseEntity.ok(salva);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Viagem> atualizar(@PathVariable Long id,
									        @RequestBody Viagem viagem) {
		Optional<Viagem> existe = repository.findById(id);
		if (existe.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		if(existe.get().getChegada().isAfter(LocalDate.now())){
			throw new IllegalArgumentException("Edição não permitidada para viagens encerradas");
		}

		Viagem salva = repository.save(viagem);
		return ResponseEntity.ok(salva);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable Long id) {
		Optional<Viagem> existe = repository.findById(id);
		if (existe.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		if(existe.get().getSaida().isAfter(LocalDate.now())){
			throw new IllegalArgumentException("Exclusão não permitidada para viagens encerradas");
		}

		List<Gasto> existeGastos = gastoRepository.findByViagem(existe.get());
		if (!existeGastos.isEmpty()) {
			throw new IllegalArgumentException("Existe gasto cadastrado para a viagem informada.");
		}
		repository.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
