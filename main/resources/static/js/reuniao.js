$(document).ready(function () {
	moment.locale('pt-BR');	
	
    var table = $('#table-reunioes').DataTable({
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
            {"width":"2%", "targets": [0]},
            {"width":"10%", "targets": [2,3]}],
        ajax: {
            url: '/reunioes/datatables/server',
            data: function( d ){       
            	d.dataInicial = $('#dataInicial').val(),         
            	d.dataFinal = $('#dataFinal').val(),
            	d.buscaAssunto = $('#buscaAssunto').val()
            }
            
        },      
        columns: [
        	 {orderable: false,
                 "className":'details-control',             
                 "data":null,
                 "defaultContent": '<i class="fas fa-chevron-down" style="color:green; vertical-align: middle;"></i>'
             },
            {data: 'assunto'},
            {orderable: false, data: 'data', render:
                function( data ) {
                    return moment(data).format('DD/MM/YYYY');
                }
            },
			{orderable: false, 
	             data: 'id',
	                render: function(id) {
	                	var excluir = "";
	                	var editar = "";
	                	if(autoridade != "autoridade"){
	                		excluir = '';
	                		editar = '';
	                	}else{
	                		excluir = '<a id="btn-del-cliente" class="btn-tabela btn-tabela-excluir" href="/reunioes/excluir/'+ 
	                		id +'" role="button" title="Excluir" data-toggle="modal" data-target="#confirm-modal"><i class="fas fa-trash-alt"></i></a>'
	                		
	                		editar = '<a class="btn-tabela btn-tabela-editar" href="/reunioes/editar/'+ 
	                    	id +'" role="button" title="Editar"><i class="fas fa-edit"></i></a>'
	                	}
	                    return editar +
	                     excluir +
	                		'<a class="btn-tabela  btn-tabela-view" href="/reunioes/visualizar/'+ 
	                       	id +'" role="button" title="Visualizar"><i class="fas fa-eye"></i></a>'
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
			},
			{
				text: '<i class="fas fa-trash-alt"></i>',
				attr: {
					id: 'btn-excluir-sm',
					type: 'button',
					class: "btn btn-tabela-responsivo btn-tabela-responsivo-excluir",
					"data-toggle":"modal",
					"data-target":"#confirm-modal"
				},
			
				enabled: false
			},
			{
				text: '<i class="fas fa-eye"></i>',
				attr: {
					id: 'btn-visualizar-sm',
					type: 'button',
					class: "btn btn-tabela-responsivo  btn-tabela-responsivo-view"
				},
				enabled: false
			} 
		]
    });
    
    table.buttons().disable();
    var alvo = '#table-reunioes'
	var objeto = "/reunioes"
    
    $("#table-reunioes thead").on('click', 'tr', function() {		
		table.buttons().disable();
	});

    fecharRowAoClicarEmBotao(alvo)
	tabelaResponsiva(alvo, table, 3, objeto);


	
    
    if($('#dataInicial').val("")){
    	$('#dataFinal').prop('disabled', true);
    	
    }
    
    $('#dataInicial, #dataFinal').on('change', function(){
    	$('#dataFinal').prop('disabled', false)
    	table.ajax.reload();
    	table.buttons().disable();
    });
    
    $('.fechar').on('click', function(){
		$('#falha').hide()
	})
	  $('#dataFinal, #dataInicial').on('change', function(){
	    	var inicio = moment($('#dataInicial').val());
	    	var fim = moment($('#dataFinal').val());
	    	if( fim <= inicio){
	    		$('#falha').show()
	    		$('#falha span strong').html('A data final não pode ultrapassar a inicial')
	      		
	      		
	        }
	    	
	    });
    $('input[type="search"], #limpar').on('click', function(){
    	$('#dataFinal').prop('disabled', true)
    	$('#dataInicial, #dataFinal, #buscaAssunto').val("");
    	table.ajax.reload();
    	table.buttons().disable();
    	
    });
    $('#buscaAssunto').on('keyup', function(){
    	table.buttons().disable();
    	table.ajax.reload();
    });
});    
function format ( d ) {
	
	var data = moment(d.data).format("DD/MM/YYYY");
	
	return '<div class="slider"><table id="inside-reunioes" class="table" style="table-layout: fixed; width: 100%;">'+
	'<tr><td scope="row" class="wrapword"><b>Assunto: </b>' + d.assunto +'</td>' +
	'<td class="wrapword"><b>Data: </b>' + data + '</td></tr>' +
	'</table></div>';
}