package br.com.etechoracio.boa_viagem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.etechoracio.boa_viagem.entity.Viagem;

import java.util.List;

public interface ViagemRepository extends JpaRepository<Viagem, Long> {

    List<Viagem> findByDestinoIgnoreCase(String destino);

}
