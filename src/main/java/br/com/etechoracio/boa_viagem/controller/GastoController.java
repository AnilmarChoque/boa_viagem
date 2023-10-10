package br.com.etechoracio.boa_viagem.controller;

import br.com.etechoracio.boa_viagem.entity.Gasto;
import br.com.etechoracio.boa_viagem.entity.Viagem;
import br.com.etechoracio.boa_viagem.exceprions.ViagemInvalidaException;
import br.com.etechoracio.boa_viagem.repository.GastoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gastos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class GastoController {

	@Autowired
	private GastoRepository repository;

	@GetMapping
	public List<Gasto> listarTodos() {
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Gasto> buscarPorId(@PathVariable Long id) {
		Optional<Gasto> existe = repository.findById(id);
		return existe.isPresent() ? ResponseEntity.ok(existe.get())
				: ResponseEntity.notFound().build();
	}

	/*@PutMapping("/{id}")
	public ResponseEntity<Viagem> atualizar(@PathVariable Long id,
									        @RequestBody Viagem viagem) {
		Optional<Viagem> existe = repository.findById(id);
		if (existe.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		if(existe.get().getSaida().isAfter(LocalDate.now())){
			throw new IllegalArgumentException("Edição não permitidada para viagens encerradas");
		}

		Viagem salva = repository.save(viagem);
		return ResponseEntity.ok(salva);
	}*/
	@PostMapping
	public ResponseEntity<Gasto> inserir(@PathVariable Long id, @RequestBody Gasto obj) {
		Optional<Gasto> existe = repository.findById(id);
		if(existe.get().getData().isAfter(LocalDate.now())){
			ViagemInvalidaException(throw new ViagemInvalidaException("Viagem finalizada não permite atua"));
		}
		Gasto salva = repository.save(obj);
		return ResponseEntity.ok(salva);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Gasto> atualizar(@PathVariable Long id,
											@RequestBody Gasto gasto) {
		Optional<Gasto> existe = repository.findById(id);
		if (existe.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Gasto salva = repository.save(gasto);
		return ResponseEntity.ok(salva);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluir(@PathVariable Long id) {
		boolean existe = repository.existsById(id);
		if (existe) {
			repository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
	
}
