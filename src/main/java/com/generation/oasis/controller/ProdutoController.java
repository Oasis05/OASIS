
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.oasis.model.Produto;
import com.generation.oasis.repository.ProdutoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@GetMapping
	public ResponseEntity<List<Produto>> getAll (){
		return ResponseEntity.ok(produtoRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> getByID(@PathVariable Long id) {
		return produtoRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/nomeProduto/{nomeProduto}")
    public ResponseEntity<List<Produto>> getByNomeProduto(@PathVariable String nomeProduto) {
        return ResponseEntity.ok(produtoRepository.findAllBynomeProdutoContainingIgnoreCase(nomeProduto));
    }

			
	@PostMapping
	public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(produtoRepository.save(produto));
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Produto> updateProduto(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
	    Produto produto = produtoRepository.findById(id).orElse(null);

	    if (produto != null) {
	        produto.setNomeProduto(produtoAtualizado.getNomeProduto());
	        produto.setPreco(produtoAtualizado.getPreco());
	        produtoRepository.save(produto);
	        return ResponseEntity.ok(produto);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete (@PathVariable Long id) {
		Optional<Produto> produto = produtoRepository.findById(id);
		
		if(produto.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		produtoRepository.deleteById(id);
	}
	
}