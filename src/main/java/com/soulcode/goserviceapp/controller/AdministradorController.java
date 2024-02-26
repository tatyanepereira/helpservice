package com.soulcode.goserviceapp.controller;

import com.soulcode.goserviceapp.domain.*;
import com.soulcode.goserviceapp.service.*;
import com.soulcode.goserviceapp.service.exceptions.ServicoNaoEncontradoException;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoAutenticadoException;
import com.soulcode.goserviceapp.service.exceptions.UsuarioNaoEncontradoException;
import org.hibernate.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdministradorController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private UsuarioLogService usuarioLogService;

    @Autowired
    private AgendamentoLogService agendamentoLogService;

    @Autowired
    private AdminService adminService;

    @GetMapping("/servicos")
    public ModelAndView getServices(@RequestParam(defaultValue = "0") int page) {
        ModelAndView mv = new ModelAndView("servicosAdmin");
        try {
            int pageSize = 10;
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<Servico> servicePage = servicoService.findNumbServicos(pageable);
            List<Servico> servicos = servicePage.getContent();
            Integer totalPages = servicePage.getTotalPages();
            mv.addObject("servicos", servicos);
            mv.addObject("currentPage", page);
            mv.addObject("totalPage", totalPages);
        } catch (Exception ex) {
            mv.addObject("errorMessage", "Erro ao buscar dados de serviços.");
        }
        return mv;
    }

    @PostMapping(value = "/servicos")
    public String createService(Servico servico, RedirectAttributes attributes) {
        try {
            servicoService.createServico(servico);
            attributes.addFlashAttribute("successMessage", "Novo serviço adicionado.");
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao adicionar novo serviço.");
        }
        return "redirect:/admin/servicos";
    }

    @PostMapping(value = "/servicos/remover")
    public String removeService(@RequestParam(name = "servicoId") Long id, RedirectAttributes attributes) {
        try {
            servicoService.removeServicoById(id);
            attributes.addFlashAttribute("successMessage", "Serviço removido.");
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao excluir serviço.");
        }
        return "redirect:/admin/servicos";
    }

    @GetMapping(value = "/servicos/editar/{id}")
    public ModelAndView editService(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("editarServico");
        try {
            Servico servico = servicoService.findById(id);
            mv.addObject("servico", servico);
        } catch (ServicoNaoEncontradoException ex) {
            mv.addObject("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            mv.addObject("errorMessage", "Erro ao buscar dados do serviço.");
        }
        return mv;
    }

    @PostMapping(value = "/servicos/pesquisa")
    public ModelAndView searchServico(@RequestParam(name = "nome-servico")String nome){
        ModelAndView mv = new ModelAndView("servicosAdmin");
        try {
            List<Servico> buscaServico = usuarioService.buscarServicosPorNome(nome);
            mv.addObject("servicos", buscaServico);
        }catch (ServicoNaoEncontradoException ex){
            mv.addObject("errorMessage", ex.getMessage());
        }catch (Exception ex){
            mv.addObject("errorMessage", "Serviço não encontrado.");
        }
        return mv;
    }

    @PostMapping(value = "/usuarios/pesquisa")
    public ModelAndView searchUsuario(@RequestParam(name = "nome-usuario")String nome){
        ModelAndView mv = new ModelAndView("usuariosAdmin");
        try {
            List<Usuario> buscaUsuario = usuarioService.buscarUsuariosPorNome(nome);
            mv.addObject("usuarios", buscaUsuario);
        }catch (UsuarioNaoEncontradoException ex){
            mv.addObject("errorMessage", ex.getMessage());
        }catch (Exception ex){
            mv.addObject("errorMessage", "Usuario não encontrado.");
        }
        return mv;
    }

    @PostMapping(value = "/servicos/editar")
    public String updateService(Servico servico, RedirectAttributes attributes) {
        try {
            servicoService.update(servico);
            attributes.addFlashAttribute("successMessage", "Dados do serviço alterados.");
        } catch (ServicoNaoEncontradoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao alterar dados do serviço.");
        }
        return "redirect:/admin/servicos";
    }

    @GetMapping(value = "/usuarios")
    public ModelAndView getUsuarios(@RequestParam(defaultValue = "0")int page) {
        ModelAndView mv = new ModelAndView("usuariosAdmin");
        try {
            int pageSize = 10;
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<Usuario> usuariosPage = usuarioService.findNumbUsuarios(pageable);
            List<Usuario> usuarios = usuariosPage.getContent();
            Integer totalPages = usuariosPage.getTotalPages();
            mv.addObject("usuarios", usuarios);
            mv.addObject("currentPage", page);
            mv.addObject("totalPage", totalPages);
        } catch (Exception ex) {
            mv.addObject("errorMessage", "Erro ao buscar dados de usuários.");
        }
        return mv;
    }

    @PostMapping(value = "/usuarios")
    public String createUser(Usuario usuario, RedirectAttributes attributes) {
        try {
            usuarioService.createUser(usuario);
            attributes.addFlashAttribute("successMessage", "Novo usuário cadastrado.");
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao cadastrar novo usuário.");
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping(value = "/usuarios/disable")
    public String disableUser(@RequestParam(name = "usuarioId") Long id, RedirectAttributes attributes) {
        try {
            usuarioService.disableUser(id);
        } catch (UsuarioNaoEncontradoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao desabilitar usuário.");
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping(value = "/usuarios/enable")
    public String enableUser(@RequestParam(name = "usuarioId") Long id, RedirectAttributes attributes) {
        try {
            usuarioService.enableUser(id);
        } catch (UsuarioNaoEncontradoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            attributes.addFlashAttribute("errorMessage", "Erro ao habilitar usuário.");
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping(value = "/dashboard")
    public ModelAndView dashboard() {
        ModelAndView mv = new ModelAndView("dashboard");

        try {
            List<UsuarioLog> logsAuth = usuarioLogService.findAll();
            List<AgendamentoLog> logsAgendamento = agendamentoLogService.findAll();

            mv.addObject("logsAuth", logsAuth);
            mv.addObject("logsAgendamento", logsAgendamento);
        } catch (Exception ex) {
            mv.addObject("errorMessage", "Erro ao buscar dados de log de autenticação ou agendamento.");
        }

        return mv;
    }

    @GetMapping(value = "/dados")
    public ModelAndView dados(Authentication authentication) {
        ModelAndView mv = new ModelAndView("dadosCadastrais");
        try {
            Administrador administrador = adminService.findAuthenticated(authentication);
            mv.addObject("administrador", administrador);
        } catch (UsuarioNaoAutenticadoException | UsuarioNaoEncontradoException ex) {
            mv.addObject("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            mv.addObject("errorMessage", "Erro ao buscar dados do administrador.");
        }
        return mv;
    }

    @PostMapping(value = "/dados")
    public String alterarDadosAdmin(Administrador administrador, RedirectAttributes attributes) {
        try {
            adminService.update(administrador);
            attributes.addFlashAttribute("successMessage", "Dados alterados.");
        } catch (UsuarioNaoEncontradoException ex) {
            attributes.addFlashAttribute("errorMessage", ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            attributes.addFlashAttribute("errorMessage", "Erro ao alterar dados cadastrais.");
        }

        return "redirect:/admin/dados";
    }



}
