bodyEstudios = document.querySelector('#BodyEstudios'); 
bodyEstudios.addEventListener('click', obtenerLogrosPorEstudio);

function busquedaLogrosPorEstudioId(estudioId){
	$Coincidencias = $('#Coincidencias').get(0);
	var param = {estudioId: estudioId};
	var url = '', notFoundMessage = '';
	if($('#AppLanguageW').val() == 1 && $('#AppLanguage').val() == 1){
		url = _ctx+'web/en/gt/logro';
		notFoundMessage = 'We are sorry, not found achievements relationship to the research.';
	}else{
		url = _ctx+'web/gt/logro';
		notFoundMessage = 'Lo sentimos, no se encontraron logros relacionados al estudio.';
	}
		$.ajax({
	        type: 'GET',
            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    
	        url: url,
            dataType: "json",
            data: param,
	        success: function (data, textStatus) {
	            if (textStatus == "success") {
	            	if(data == "-88"){
	            		notFoundMessage = "Ha realizado una consulta incorrecta. Por favor contactarse con la administración. Gracias.";
	            		$Coincidencias.innerHTML = `<div class="estudio01"><h3 style="color: #f97c7f;"><i class="fa fa-exclamation-circle"></i> ${notFoundMessage}</h3></div>`;
	            	}else{
	            		if(data.length >0){
			            	var logrosRaw =``;
				            	data.forEach((l, i) => {
				            		logrosRaw +=`<ul class="alcance">
									                <li class="no-ls">${l.descripcion}</li>
				            					</ul>`;
				            	});
				            	$Coincidencias.innerHTML = logrosRaw;
		            	}else{
		            		$Coincidencias.innerHTML = `<div class="estudio01"><h3 style="color: #f97c7f;"><i class="fa fa-exclamation-circle"></i> ${notFoundMessage}</h3></div>`;	
		            	}
	            	}
	            	
	            }
	        },
	        error: function (xhr, ajaxOptions, thrownError) {
	        	exception(xhr["status"], xhr["responseJSON"]["error"]);
	        }, complete: function(){}
	    });
	
}

function obtenerLogrosPorEstudio(e){
	if(e.target.classList == 'title-estudio'){
		EstudioId = e.target.getAttribute('data-id');
		busquedaLogrosPorEstudioId(EstudioId);
	}
}