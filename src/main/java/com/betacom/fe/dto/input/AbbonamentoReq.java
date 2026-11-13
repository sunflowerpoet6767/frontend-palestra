package com.betacom.fe.dto.input;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AbbonamentoReq {
	private Integer id;
	private LocalDate dataAbbonamento;
	private Integer durataAbbonamento;
	private Integer socioID;
	private Integer attivitaID;
	

}
