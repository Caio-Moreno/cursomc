package com.caiomoreno.cursomc.services;

import com.caiomoreno.cursomc.domain.PagamentoComBoleto;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
public class BoletoService {
    public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instante) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(instante);
        cal.add(Calendar.DAY_OF_MONTH, 7);
        pagto.setDataVencimento(cal.getTime());
    }
}
