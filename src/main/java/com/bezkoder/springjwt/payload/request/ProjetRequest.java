package com.bezkoder.springjwt.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ProjetRequest {
    @NotBlank
    @Size(max = 100)
    private String nomProjet;

    @NotBlank
    @Size(max = 20)
    private String codeProjet;

    public ProjetRequest() {
    }

    public ProjetRequest(String nomProjet, String codeProjet) {
        this.nomProjet = nomProjet;
        this.codeProjet = codeProjet;
    }

    public String getNomProjet() {
        return nomProjet;
    }

    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public String getCodeProjet() {
        return codeProjet;
    }

    public void setCodeProjet(String codeProjet) {
        this.codeProjet = codeProjet;
    }
}
