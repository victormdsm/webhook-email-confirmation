package com.aula.pessoa.service;

import com.aula.pessoa.config.exceptions.EntityNotFoundException;
import com.aula.pessoa.config.exceptions.FailedSaveException;
import com.aula.pessoa.config.mail.EmailSenderImpl;
import com.aula.pessoa.entities.Pessoa;
import com.aula.pessoa.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository repository;
    private final EmailSenderImpl emailSender;


    public Pessoa findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado"));
    }

    public List<Pessoa> findAll() {
        return repository.findAll();
    }

    @Transactional
    public Pessoa save(Pessoa pessoa) {
        try {
            var savedPessoa = repository.save(pessoa);

            String linkConfirmacao = "http://localhost:8080/confirm?id=" + savedPessoa.getId();

            emailSender.sendTemplateEmail(savedPessoa.getNome(), linkConfirmacao, savedPessoa.getEmail());

            return savedPessoa;

        } catch (DataIntegrityViolationException e) {
            throw new FailedSaveException("Falha ao salvar, email ou cpf ja cadastrados");
        }
    }

    public Pessoa update(long id, Pessoa pessoa) {
        try {
            var p = this.findById(id);
            if(pessoa.getNome() != null && !pessoa.getNome().isEmpty()) {
                p.setNome(pessoa.getNome());
            }
            if(pessoa.getEmail() != null && !pessoa.getEmail().isEmpty()) {
                p.setEmail(pessoa.getEmail());
            }
            if(pessoa.getCpf() != null && !pessoa.getCpf().isEmpty()) {
                p.setCpf(pessoa.getCpf());
            }
            return repository.save(p);
        } catch (DataIntegrityViolationException e) {
            throw new FailedSaveException("Falha ao atualizar, email ou cpf ja cadastrados");
        }
    }

    public void delete(long id) {
        var p = this.findById(id);
        repository.delete(p);
    }

    public void confirm(Long id){
        var user = this.findById(id);
        user.setIsActive(true);
        repository.save(user);
    }


}
