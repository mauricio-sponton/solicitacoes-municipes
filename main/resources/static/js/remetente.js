$(document).ready(function () {
	moment.locale('pt-BR');
    var table = $('#table-remetentes').DataTable({
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
        ajax: {
            url: '/remetentes/datatables/server',
            data: 'data'
           
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
              targets: [2, 1],
              render: $.fn.dataTable.render.ellipsis( 20 )
            },
            {"width":"1%", "targets": [0]},
            {"width":"5%", "targets": [3]}],
        columns: [
        	{orderable: false,
                "className":'details-control',             
                "data":null,
                "defaultContent": '<i class="fas fa-chevron-down" style="color:green; vertical-align: middle;"></i>'
            },
            {data: 'nome'},
            {data: 'logradouro',  "render": function(data, type, row){
            	return row.logradouro + " " + row.numero + ", " + row.bairro + " - " + row.cidade + "/ " + row.uf;
            }},
            {orderable: false, 
             data: 'id',
                "render": function(id) {
                	var excluir = "";
                	
                	if(autoridade != "autoridade"){
                		excluir = ''
                	}else{
                		excluir = '<a id="btn-del-cliente" class="btn-tabela  btn-tabela-excluir" href="/remetentes/excluir/'+ 
                    	id +'" role="button" title="Excluir" data-toggle="modal" data-target="#confirm-modal"><i class="fas fa-trash-alt"></i></a>'
                	}
                    return '<a class="btn-tabela btn-tabela-editar" href="/remetentes/editar/'+ 
                    	id +'" role="button" title="Editar"><i class="fas fa-edit"></i></a>' +
                    	excluir;
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
			}
		]
    });
    table.buttons().disable();
    
    if(autoridade != "autoridade"){
		$('#btn-excluir-sm').hide()
	}
    
    var alvo = '#table-remetentes';
    var objeto = "/remetentes"
    
    $("#table-remetentes thead").on('click', 'tr', function() {		
		table.buttons().disable();
	});
    fecharRowAoClicarEmBotao(alvo)
	tabelaResponsiva(alvo, table, 2, objeto);
    
});    
function format ( d ) {
	
	return '<div class="slider"><table id="inside-assuntos" class="table" style="table-layout: fixed; width: 100%;">'+
	'<tr><td scope="row" class="wrapword"><b>Descrição: </b>' + d.nome +'</td></tr>' +
	'<tr><td class="wrapword"><b>Ativo? </b>' + d.logradouro + " " + d.numero + ", " + d.bairro + " - " + d.cidade + "/ " + d.uf + '</td></tr>' +
	'</table></div>';
}     