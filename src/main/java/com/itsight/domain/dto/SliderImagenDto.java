package com.itsight.domain.dto;

import lombok.Data;

@Data
public class SliderImagenDto {

    private int tipoImagen;
    private String nombreMedia;

    public SliderImagenDto(){}

    public SliderImagenDto(int tipoImagen, String nombreMedia) {
        this.tipoImagen = tipoImagen;
        this.nombreMedia = nombreMedia;
    }
}
