bodyFechas = document.querySelector('#BodyFechas'); 
bodyFechas.addEventListener('click', obtenerFechaConsulta);

$(function(){
    getFechasFormateadas();
	$('#btnConsultar').click(function(e){
		e.preventDefault();
		busquedaPorTitulo('','');
	});

});

function busquedaPorFecha(fecha){
	$Coincidencias = $('#Coincidencias').get(0);
	benId = parseInt($('#BeneficiarioId').text());
	var url = '', notFoundMessage = '';
	if($('#AppLanguageW').val() == 1 && $('#AppLanguage').val() == 1){
		url = _ctx+'web/en/gt/estudio';
		notFoundMessage = 'We are sorry, not found matches.';
	}else{
		url = _ctx+'web/gt/estudio';
		notFoundMessage = 'Lo sentimos, no se encontraron coincidencias.';
	}
	
		$.ajax({
	        type: 'GET',
            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
	        url: url,
            dataType: "json",
            data: {fecha: fecha, beneficiarioId: benId},
	        success: function (data, textStatus) {
	            if (textStatus == "success") {
	            	if(data.length >0){
		            	var estudiosRaw =``;
			            	data.forEach((r, i) => {
			            		estudiosRaw +=`<div class='estudio01'>
			            							<a href='${_ctx + r.url + r.id}' class='title'>
			            								<h2><strong>${r.titulo}</strong></h2>
			            							</a>
			            					   </div>`;
			            	});
			            	$Coincidencias.innerHTML = estudiosRaw;
	            	}else{
	            		$Coincidencias.innerHTML = `<div class="estudio01"><h3 style="color: #f97c7f;"><i class="fa fa-exclamation-circle"></i>  ${notFoundMessage}</h3></div>`;	
	            	}
	            }
	        },
	        error: function (xhr, ajaxOptions, thrownError) {
	        	notFoundMessage = "Ha realizado una consulta incorrecta. Por favor contactarse con la administración. Gracias.";
        		$Coincidencias.innerHTML = `<div class="estudio01"><h3 style="color: #f97c7f;"><i class="fa fa-exclamation-circle"></i> ${notFoundMessage}</h3></div>`;        		
	        }, complete: function(){
	        	$('#Titulo').val("");
	        }
	    });
}

function obtenerFechaConsulta(e){
	if(e.target.classList == 'body-fecha'){
		fecha = e.target.getAttribute('data-fecha');
		busquedaPorFecha(fecha);
	}
}

function getFechasFormateadas(){
    if($('#AppLanguageW').val() == 1 && $('#AppLanguage').val() == 1){
    	const beneficiarioId = $('#BeneficiarioId').text();
        $.ajax({
            type: 'GET',
            contentType : "application/json",
            url: _ctx+'web/en/research/get/dates/sec/'+beneficiarioId,
            dataType: "json",
            success: function (data, textStatus) {
                if (textStatus == "success") {
                    let fechasFormat = `
										<div>
											<ul>
											  ${getBodyFechas(data)}
											</ul>
										</div>
									   `;
                    $('#BodyFechas').html(fechasFormat);
                }
            }, complete: function(){}
        });
    }
}

function getBodyFechas(fechas){
    let d = '';
    fechas.forEach(fc=>{
        const arrDate = fc.split("-");
        const event = new Date(Date.UTC(arrDate[0], Number(arrDate[1]), 0, 0, 0, 0));
        const options = {month: 'long', year: 'numeric'};
        const dtFormat = event.toLocaleDateString('en', options);
        d += `<li>
				<a href="#">
					<p data-fecha="${fc}" class="body-fecha">${dtFormat}</p>
				</a>
			</li>`;
    })
    return d;
}