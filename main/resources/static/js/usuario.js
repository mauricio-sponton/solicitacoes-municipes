$(document).ready(function() {
	   $('.pass').on('keyup',function(){
			if($('.senha').val() != $('.senha-repetida').val()){
				$('.senhas').show().text('As senhas não são iguais!');
				$('.salvar-user').prop('disabled', true);
			}else{
				$('.senhas').hide();
				$('.salvar-user').prop('disabled', false);
			}
				
		});
	   $('.pass-modal').on('keyup',function(){
			if($('#senha-modal').val() != $('#senha-repetida-modal').val()){
				$('#senhas-modal').show().text('As senhas não são iguais!');
				$('#salvar-user-modal').prop('disabled', true);
			}else{
				$('#senhas-modal').hide();
				$('#salvar-user-modal').prop('disabled', false);
			}
				
		})
	
	$(".senha").passwordValidation({"confirmField": ".senha-repetida"}, function(element, valid, match, failedCases) {
		  $(".errors").html("<pre>" + failedCases.join("\n") + "</pre>");
		   if(valid) $(element).css("border","2px solid green");
		   if(!valid) $(element).css("border","2px solid red");
		   if(valid && match) $(".senha-repetida").css("border","2px solid green");
		   if(!valid || !match){
			   $(".senha-repetida").css("border","2px solid red");		
			   $('.salvar-user').prop('disabled', true);
		   }
		
	});
	   $("#senha-modal").passwordValidation({"confirmField": "#senha-repetida-modal"}, function(element, valid, match, failedCases) {
			  $("#errors-modal").html("<pre>" + failedCases.join("\n") + "</pre>");
			 			   
			   if(!valid || !match){
				   $("#senha-repetida-modal").css("border","2px solid red");		
				   $('#salvar-user-modal').prop('disabled', true);
			   }
			
		});
	   $('.pass').keyup(function(){
			if($('.senha').val() === $('.senha-repetida').val() && $('.errors').text() == ""){
		   		$('#senha3').removeAttr('readonly')
	   		}else{
	   			$('#senha3').attr('readonly', 'readonly');
	   		}
				
				
		});

	
	
	moment.locale('pt-BR');
	var table = $('#table-usuarios').DataTable({
		"language":{
    		"lengthMenu": "Mostrando _MENU_ registros por páginas",
    		"zeroRecords": "Nenhum registro encontrado",
            "info": "Mostrando página _PAGE_ de _PAGES_",
            "infoEmpty": " ",
            "search": "Pesquise: ",
            "searchPlaceholder": "Email ou Perfil...",
            "paginate": {
                "first":      "Primeiro",
                "last":       "Ultimo",
                "next":       "Próximo",
                "previous":   "Anterior"
            } 
    	},
		searching : true,
		lengthMenu : [ 20 ],
		
		serverSide : true,
		responsive: {
            details: false
        },
		ajax : {
			url : '/u/datatables/server/usuarios',
			data : 'data'
		},
        drawCallback: function () {
    		  $('[data-toggle="popover-hover"]').popover({
  			      html: true,
  			      trigger: 'hover',
  			      placement: 'right'	      		   
  		  })
          
    		 
    		  
    		  $('.paginate_button:not(.active)', this.api().table().container())          
    	         .on('click', function(){
    	        	 table.buttons().disable();
    	         });   
    	    },
   
          columnDefs: [ {
              targets: [0, 1],
              render: $.fn.dataTable.render.ellipsis( 20 )
            },
            {"width":"5%", "targets": [5]},
            {"width":"1%", "targets": [0]},],
		columns : [
			 {orderable: false,
                 "className":'details-control',             
                 "data":null,
                 "defaultContent": '<i class="fas fa-chevron-down" style="color:green; vertical-align: middle;"></i>'
             },
				{data : 'nome'},
				{data : 'email'},
				{	data : 'ativo', 
					render : function(ativo) {
						return ativo == true ? 'Sim' : 'Não';
					},
					orderable : false,
				},
				{	data : 'perfis',									 
					render : function(perfis) {
						var aux = new Array();
						$.each(perfis, function( index, value ) {
							  aux.push(value.desc);
						});
						return aux;
					},
					orderable : false,
				},
				{	data : 'id',	
					render : function(id) {
						return '<a class="btn-tabela btn-tabela-editar" href="/u/editar/credenciais/usuario/' + 
						id + '" role="button" title="Editar" data-toggle="tooltip" data-placement="right"><i class="fas fa-edit"></i></a>' +
						'<a class="btn-tabela btn-tabela-editar" href="/u/editar/senha/usuario/' + 
							id + '" role="button" title="Editar Senha" data-toggle="tooltip" data-placement="right"><i class="fas fa-unlock-alt"></i></a>';
					},
					orderable : false
				}          
			
		],
		 dom: 'Bfrtip',
		 buttons: [
				{
					text: '<i class="fas fa-edit"></i>',
					attr: {
						
						id: 'btn-editar-sm',
						type: 'button',
						class: "btn btn-tabela-responsivo btn-tabela-responsivo-editar",
						
					},
					enabled: false
				}
			]
	});
	 table.buttons().disable();
	 
	 $("#table-usuarios thead").on('click', 'tr', function() {		
			table.buttons().disable();
		});
	    
	    
	    $("#table-usuarios tbody").on('click', 'a', function() {
	    	
			var tr = $('#table-usuarios tbody').closest('tr');
	        var row = table.row( tr );
	        row.child( formatUsuarios(row.data()), 'no-padding' ).hide();
		});
		
		 $('#table-usuarios tbody').on('click', 'tr', function () {
				
			 
		        var tr = $(this).closest('tr');
		        tr.addClass('tr-fundo')
		        var row = table.row( tr );
		 
		        var link = row.data().id;
				$('#btn-editar-sm').attr("href", '/u/editar/credenciais/usuario/' + link);
				
		
				table.buttons().enable();
		       
				if ( row.child.isShown()) {
		            // This row is already open - close it
		        	 $('div.slider', row.child()).slideUp( function () {
		                 row.child.hide();
		                 tr.removeClass('shown');
		                 
		             } );
		        	 tr.removeClass('tr-fundo');
		        	 if(!tr.hasClass('tr-fundo')){
		        		 table.buttons().disable();
		        	 }
		        	
		 			   
		        	 $('#btn-editar-sm').attr("href", '/u/editar/credenciais/usuario/' + link);
						
		        }
		        else {
		        	if ( table.row( '.shown' ).length ) {
		                $('.details-control', table.row( '.shown' ).node()).click();
		        }
		            // Open this row
		        	 if(tr.hasClass('tr-fundo')){
		        		 table.buttons().enable();
		        	 }
		        	
		            row.child( formatUsuarios(row.data()), 'no-padding' ).show();	        
		            tr.addClass('shown');
		            $('div.slider', row.child()).slideDown();
		            $('#inside tbody').on('click', 'tr', function (e){	
		            	e.stopPropagation();	
		            			
		            })   
		            $('html, body').animate({
			              scrollTop: $(".slider").offset().top-200
			          }, 2000);
		            $('#btn-editar-sm').attr("href", '/u/editar/credenciais/usuario/' + link);
					
		        }
		    } );
		
		 $('#btn-editar-sm').on('click', function(){
				var link = $(this).attr('href');
				document.location.href = link;
		});
//		 $('#btn-excluir-sm').on('click', function(){
//				url = $(this).attr('href');
//		})
	$('#table-usuarios tbody').on('click', '[id*="dp_"]', function(){
		var data = table.row($(this).parents('tr')).data();
		var aux = new Array();
		$.each(data.perfis, function(index, value){
			aux.push(value.id);
		});
		document.location.href = '/u/visualizar/dados/usuario/' + data.id + '/perfis/' + aux;
	});
});	
function formatUsuarios ( d ) {

	var ativo = "";
	if(d.ativo == true){
		ativo = "Sim"
	}else{
		ativo = "Não"
	}
	
	var aux=[]
	$.each(d.perfis, function(k,v){
		aux.push(v.desc);
		
	})
	
	return '<div class="slider"><table id="inside" class="table" style="table-layout: fixed; width: 100%">'+
	'<tr><td class="wrapword"><b>Nome de Usuário (Email): </b>' + d.email + '</td>'+
	'<td scope="row" class="wrapword"><b>Cadastro Ativo? </b>' + ativo + '</td></tr>'+
	'<tr><td scope="row" class="wrapword"><b>Nome: </b>' + d.nome +'</td>' +
	'<td class="wrapword"><b>Perfis: </b>' + aux.join(' ') + '</td></tr>'+
	'</table></div>';
}


