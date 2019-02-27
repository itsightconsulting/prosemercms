bodyTags = document.querySelector('#BodyTags');
bodyTags.addEventListener('click', consultaPorTags);
bodyFechas = document.querySelector('#BodyFechas'); 
bodyFechas.addEventListener('click', obtenerFechaConsulta);

$(function(){

    getFechasFormateadas();
    obtenerTags();
	$('#btnConsultar').click(function(e){
		e.preventDefault();
		busquedaPorTitulo('','');
	});

});

function busquedaPorTitulo(fecha, tags){
	$Coincidencias = $('#Coincidencias').get(0);
	var titulo = $('#Titulo').val();
	if(titulo.length > 2 || fecha.length > 0 || tags.length > 0){
		var params = {titulo: titulo};
		if(fecha.length > 0){
			params.fecha = fecha;
		}
		if(tags.length > 0){
			params.tags = tags;
		}
		
		var url = '', notFoundMessage = '';
		if($('#AppLanguageW').val() == 1 && $('#AppLanguage').val() == 1){
			url = _ctx+'web/en/gt/evento';
			notFoundMessage = 'We are sorry, not found matches.';
		}else{
			url = _ctx+'web/gt/evento';
			notFoundMessage = 'Lo sentimos, no se encontraron coincidencias.';
		}
		
		$.ajax({
	        type: 'GET',
            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
	        url: url,
            dataType: "json",
            data: params,
	        success: function (data, textStatus) {
	            if (textStatus == "success") {
	            	if(data.length >0){
		            	var estudiosRaw =``;
			            	data.forEach((r, i) => {
			            		estudiosRaw +=`<div class='estudio01'>
			            							<a href='${_ctx + r.url + r.id}' class='title'>
			            								<h2 class="itz">${r.titulo}</h2>
			            							</a>
			            							<p class='form-group'>${r.resumenRaw}</p>
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
	        }, complete: function(data){
	        	$('#Titulo').val("");
	        }
	    });
	}else{
		$('#Alerta').css('display','');
		$('#Alerta').delay(4000).fadeOut();
	}
	
}

function obtenerFechaConsulta(e){
	if(e.target.classList == 'body-fecha'){
		fecha = e.target.getAttribute('data-fecha');
		busquedaPorTitulo(fecha,'');
	}
}

function consultaPorTags(e){

	if(e.target.classList.contains('bd-tags')){
		tag = e.target.textContent.trim();
		busquedaPorTitulo('',tag);
	}
}

function getFechasFormateadas(){
    if($('#AppLanguageW').val() == 1 && $('#AppLanguage').val() == 1){
        $.ajax({
            type: 'GET',
            contentType : "application/json",
            url: _ctx+'web/en/events/get/dates',
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

function obtenerTags(){
    const bodyTags = document.querySelector('#BodyTags .list-inline');

    var url = '';
    if($('#AppLanguageW').val() == 1 && $('#AppLanguage').val() == 1){
        url = _ctx+'web/en/gt/evento/tags';
        notFoundMessage = 'We are sorry, not found matches.';
    }else{
        url = _ctx+'web/gt/evento/tags';
        notFoundMessage = 'Lo sentimos, no se encontraron coincidencias.';
    }

    $.ajax({
        type: 'GET',
        contentType : "application/x-www-form-urlencoded; charset=UTF-8",
        url: url,
        dataType: "json",
        success: function (data, textStatus) {
            if (textStatus == "success") {
                let tags = [], tagsList = '';
                data.forEach(tagsConcat=>{
                    tags = tags.concat(tagsConcat.split(","));
                })
                tags = tags.map(v => {return v.trim()});
                tags = Array.from(new Set(tags));
                tags.sort();
                tags.forEach(t=>{
                    tagsList+= `<li>
								  <a href="#">
									<p class="badge badge-info bd-tags">${t}</p>
								  </a>
								</li>`
                })
                bodyTags.innerHTML = tagsList;
            }
        },
        error: function () {},
        complete: function(){}
    });
}
