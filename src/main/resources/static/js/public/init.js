var _ctx = $('meta[name="_ctx"]').attr('content');
$(function () {
	carousel();

	$(window).resize(function() {
		carousel();
	});	
	
	$('#AppLanguage').change(function(){
		if(this.value == 0){
			window.location.href = _ctx+"web/inicio";
		}else{
			window.location.href = _ctx+"web/en";
		}
	});
	
	$('#AppLanguageW').change(function(){
		if(this.value == 0){
			window.location.href = _ctx+"web/inicio";
		}else{
			window.location.href = _ctx+"web/en";
		}
	});
	
	initGallery();
	referenciaPaginaPrincipal();
    if($(document).width() < 500){
        $('#CanadianFlag').removeAttr('height');
        $('#CanadianFlag').removeAttr('width');
    }else{

    }
	
})
function initGallery(){
	var gallery = document.querySelector('#myGallery .item');
	if(typeof(gallery) == 'object' && gallery !=null){
		gallery.classList.add("active");
	}
}
function referenciaPaginaPrincipal(){
	if(window.location.href.includes("/en"))
		document.querySelector('#EnlaceInicio a').href = _ctx+'web/en'; 
}
function carousel(){
	let ancho = window.innerWidth;

	if (ancho > 800){
		$('.owl-carousel').owlCarousel('destroy'); 
		$('.owl-carousel').owlCarousel({ 
			autoWidth:false,
			margin: 38,
			loop:true, 
			nav:true, 
			// merge:true,
			responsive:{ 0:{ items:1 } , 768:{ items: 3 }} 
		}) 
	} else {
		$('.owl-carousel').owlCarousel('destroy'); 
		$('.owl-carousel').owlCarousel({ 
			autoWidth:true,
			margin: 38,
			loop:false, 
			nav:false, 
			merge:true,
			responsive:{ 0:{ items:1 } , 768:{ items: 3 }} 
		}) 
	}
		

	// $('.owl-carousel').owlCarousel({
	//     loop:true,
	//     margin:65,
	//     responsiveClass:true,
	//     responsive:{
	//         0:{
	//         	margin: 38,
	//             items:1,
	//             nav:false
	//         },
	//         768:{

	//             items:3,
	//             nav:true
	//         }
	//     }
	// })
	
}

const menu = document.getElementById("menu");

const togglemenu = {
    mostrarMenu: function() {
        menu.classList.add("active"); 
    },
    ocultarMenu: function () {
        menu.classList.remove("active");
    }
}