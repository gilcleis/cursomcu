package com.gilclei.cursomc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.gilclei.cursomc.domain.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
	
	/**
	 *
	 * @param nome: Nome do Estado
	 * @return lista com todos os estados com nome passado como
	 *         parâmetro da função
	 */
	List<Estado> findByNomeIgnoreCase(String nome);
	
	/**
	 *
	 * @param sigla: Sigla do Estado
	 * @return lista com todos os estados com sigla passado como
	 *         parâmetro da função
	 */
	List<Estado> findBySiglaIgnoreCase(String sigla);
	

	@Query(value = "SELECT * FROM estados e "
			+ "WHERE e.id != :id and e.nome = :nome" , nativeQuery = true)
	List<Estado> findByNotIdAndNome(Integer id, String nome);
	
	@Query(value = "SELECT * FROM estados e "
			+ "WHERE e.id != :id and e.sigla = :sigla" , nativeQuery = true)
	List<Estado> findByNotIdAndSigla(Integer id, String sigla);

}
