$(document).ready(function () {
	moment.locale('pt-BR');
	
	
	
    var table = $('#table-bairros').DataTable({
    	"language":{
    		"lengthMenu": "Mostrando _MENU_ registros por páginas",
    		"zeroRecords": "Nenhum registro encontrado",
            "info": "Mostrando página _PAGE_ de _PAGES_",
            "infoEmpty": " ",
            "search": "Pesquise: ",
            "searchPlaceholder": "Nome ou Email...",
            "paginate": {
                "first":      "Primeiro",
                "last":       "Ultimo",
                "next":       "Próximo",
                "previous":   "Anterior"
            } 
    	},
    	searching: true,
    	order: [[ 0, "asc" ]],
    	lengthMenu: [20],
        serverSide: true,
        responsive: {
            details: false
        },
        drawCallback: function () {
    		  $('[data-toggle="popover-hover"]').popover({
  			      html: true,
  			      trigger: 'hover',
  			      placement: 'right'
  			      
  			   
  		  });
    		  $('.paginate_button:not(.active)', this.api().table().container())          
   	         .on('click', function(){
   	        	 table.buttons().disable();
   	         });  
    	    },
   
          columnDefs: [ {
              targets: [1],
              render: $.fn.dataTable.render.ellipsis( 30 )
            },
            {"width":"1%", "targets": [0]},
            {"width":"10%", "targets": [3,2]} ],
        ajax: {
            url: '/bairros/datatables/server'
            
        },      
        columns: [
        	 {orderable: false,
                 "className":'details-control',             
                 "data":null,
                 "defaultContent": '<i class="fas fa-chevron-down" style="color:green; vertical-align: middle;"></i>'
             },
            {data: 'descricao'},
            {data : 'ativo', 
				render : function(ativo) {
					return ativo == true ? 'Sim' : 'Não';
				}
			}, 
			{orderable: false, 
	             data: 'id',
	                render: function(id) {
//	                	var excluir = "";
//	                	
//	                	if(autoridade != "autoridade"){
//	                		excluir = ''
//	                	}else{
//	                		excluir =  '<a id="btn-del-cliente" class="btn-tabela btn-tabela-excluir" href="/bairros/excluir/'+ 
//	                		id +'" role="button" title="Excluir" data-toggle="modal" data-target="#confirm-modal"><i class="fas fa-trash-alt"></i></a>'	
//	                	}
	                    return '<a class="btn-tabela btn-tabela-editar" href="/bairros/editar/'+ 
	                    	id +'" role="button" title="Editar"><i class="fas fa-edit"></i></a>'

//	                    	+
//	                    excluir                	
	                }
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
    
    if(autoridade != "autoridade"){
		$('#btn-excluir-sm').hide()
	}
    
    var alvo = '#table-bairros';
    var objeto = "/bairros"
    
    $("#table-bairros thead").on('click', 'tr', function() {		
		table.buttons().disable();
	});
    fecharRowAoClicarEmBotao(alvo)
	tabelaResponsiva(alvo, table, 2, objeto);
    
});    
function format ( d ) {
	
	var ativo = "";
	
	if(d.ativo == true){
		ativo = "Sim";
	}else{
		ativo = "Não";
	}
	
	return '<div class="slider"><table id="inside-assuntos" class="table" style="table-layout: fixed; width: 100%;">'+
	'<tr><td scope="row" class="wrapword"><b>Descrição: </b>' + d.descricao +'</td>' +
	'<td class="wrapword"><b>Ativo? </b>' + ativo + '</td></tr>' +
	'</table></div>';
} 