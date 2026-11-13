
package com.betacom.fe.controllers;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;

import com.betacom.fe.dto.input.AttivitaReq;
import com.betacom.fe.dto.output.AttivitaDTO;
import com.betacom.fe.dto.output.ResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final WebClient webclient;

    @GetMapping("listAttivita")
    public ModelAndView listAttivita() {

        ModelAndView mav = new ModelAndView("admin/listAttivita");

        List<AttivitaDTO> attivita = webclient.get()
                .uri("attivita/list")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<AttivitaDTO>>() {})
                .block();

        mav.addObject("listAttivita", attivita);

        return mav;
    }

    @GetMapping("createAttivita")
    public ModelAndView createAttivita() {

        ModelAndView mav = new ModelAndView("admin/createAttivita");

        mav.addObject("attivita", new AttivitaReq());
        mav.addObject("titolo", "Nuova attività");

        return mav;
    }

    @PostMapping("saveAttivita")
    public String saveAttivita(
            @ModelAttribute("attivita") AttivitaReq req,
            RedirectAttributes ra) {

        ResponseEntity<ResponseDTO> response = webclient.post()
                .uri("attivita/create")
                .bodyValue(req)
                .exchangeToMono(resp -> resp.toEntity(ResponseDTO.class))
                .block();

        if (!response.getStatusCode().is2xxSuccessful()) {

            ra.addFlashAttribute("errorMsg",
                    response.getBody() != null
                        ? response.getBody().getMsg()
                        : "Errore durante la creazione dell'attività");

            return "redirect:/admin/createAttivita";
        }

        log.debug("Attività creata: {}", response.getBody());

        return "redirect:/admin/listAttivita";
    }
    
    @GetMapping("updateAttivita")
    public ModelAndView updateAttivita(@RequestParam Integer id) {

        ModelAndView mav = new ModelAndView("admin/createAttivita");

        List<AttivitaDTO> attivita = webclient.get()
                .uri("attivita/list")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<AttivitaDTO>>() {})
                .block();

        AttivitaDTO att = attivita.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Attività non trovata"));

        AttivitaReq req = new AttivitaReq();

        req.setId(att.getId());
        req.setDescrizione(att.getDescrizione());
        req.setPrezzo(att.getPrezzo());

        mav.addObject("attivita", req);
        mav.addObject("titolo", "Modifica attività");

        return mav;
    }
    
    @PostMapping("saveUpdateAttivita")
    public String saveUpdateAttivita(
            @ModelAttribute("attivita") AttivitaReq req,
            RedirectAttributes ra) {

        ResponseEntity<ResponseDTO> response = webclient.patch()
                .uri("attivita/update")
                .bodyValue(req)
                .exchangeToMono(resp -> resp.toEntity(ResponseDTO.class))
                .block();

        if (!response.getStatusCode().is2xxSuccessful()) {

            ra.addFlashAttribute("errorMsg",
                    response.getBody() != null
                        ? response.getBody().getMsg()
                        : "Errore durante l'aggiornamento dell'attività");

            return "redirect:/admin/updateAttivita?id=" + req.getId();
        }

        log.debug("Attività aggiornata: {}", response.getBody());

        return "redirect:/admin/listAttivita";
    }
    
    @PostMapping("removeAttivita")
    public String removeAttivita(
            @RequestParam Integer id,
            RedirectAttributes ra) {

        ResponseEntity<ResponseDTO> response = webclient.delete()
                .uri("attivita/delete/{id}", id)
                .exchangeToMono(resp -> resp.toEntity(ResponseDTO.class))
                .block();

        if (response == null || !response.getStatusCode().is2xxSuccessful()) {

            ra.addFlashAttribute("errorMsg",
                    response != null && response.getBody() != null
                        ? response.getBody().getMsg()
                        : "Errore durante l'eliminazione dell'attività");

            return "redirect:/admin/listAttivita";
        }

        log.debug("Attività eliminata: {}", response.getBody());

        ra.addFlashAttribute("successMsg", "Attività eliminata correttamente");

        return "redirect:/admin/listAttivita";
    }
}
