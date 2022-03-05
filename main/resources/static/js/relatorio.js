function solicitacoes ( d ) {
 			return `<div class="slider mt-2 mb-2"><div class="col-md-11" style="margin-left:7%"><table id ="nested"
			class="table table-sm table-striped nowrap display table-solicitacoes" style="width: 100%">
			<thead class="thead-dark">
				<tr>					
					<th scope="col">Assunto</th>
					<th scope="col">Bairro</th>
					<th scope="col">Usuário</th>
					<th scope="col">Data</th>
					<th scope="col">Ações</th>
				</tr>
			</thead>
		</table></div></div>`
 			 
}
function solicitacoesGroup ( d ) {
		return `<div class="slider mt-2 mb-2"><div class="col-md-12" style="margin-left:1%;"><table id ="nested"
	class="table table-sm table-striped nowrap display table-resultados" style="width: 100%">
	<thead class="thead-dark">
		<tr>
			
			<th scope="col">Assunto</th>
			<th scope="col">Bairro</th>
			<th scope="col">Usuário</th>
			<th scope="col">Data</th>
			<th scope="col">Resultado</th>
			<th scope="col">Aviso</th>
			<th scope="col">Ações</th>
		</tr>
	</thead>
</table></div></div>`
		 
}
function solicitacoesPorBairro ( d ) {
		return `<div class="slider-bairro mt-2 mb-2"><div class="col-md-11" style="margin-left:4%"><table id ="nested" 
	class="table table-sm table-striped nowrap table-solicitacoes-bairros" style="width: 100%">
	<thead class="thead-dark">
		<tr>			
			<th scope="col">Munícipe</th>
			<th scope="col">Assunto</th>
			<th scope="col">Usuário</th>
			<th scope="col">Data</th>
			<th scope="col">Ações</th>
		</tr>
	</thead>
</table></div></div>`
		 
}
function solicitacoesPorBairroESolucao ( d ) {
	return `<div class="slider-bairro mt-2 mb-2"><div class="col-md-11" style="margin-left:4%"><table id ="nested" 
class="table table-sm table-striped nowrap table-solicitacoes-bairros" style="width: 100%">
<thead class="thead-dark">
	<tr>			
		<th scope="col">Munícipe</th>
		<th scope="col">Assunto</th>
		<th scope="col">Usuário</th>
		<th scope="col">Data</th>
		<th scope="col">Resultado</th>
		<th scope="col">Aviso</th>
		<th scope="col">Ações</th>
	</tr>
</thead>
</table></div></div>`
	 
}
 		function carregaTabela(id, color, status){
 			var tableSolicitacao = $('.table-solicitacoes').DataTable({
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
 		    	 "dom": 'frtp',
 		    	searching: true,
 		 
 		    	lengthMenu: [20],			    	
 		        serverSide: true,
 		       responsive: {
 		            details: false
 		        }, 
 		        ajax: {
 		            url: '/solicitacoes/datatables/server/' + id,
 		            data: function(d){
 		            	d.status = status
 		            }
 		           
 		        },
 		       drawCallback: function () {
	 		  		  $('[data-toggle="popover-hover"]').popover({
	 					      html: true,
	 					      trigger: 'hover',
	 					      placement: 'right'			   
	 				  })
	 		  	    },	 	
 		        "columnDefs":[
 		        	{"width":"10%", "targets": [4]},
 		        	{
 	 		            targets: [0, 1, 2],
 	 		            render: $.fn.dataTable.render.ellipsis( 10 )
 	 		          }
 		        ],
 		        columns: [		        
 		            {data: 'assunto.descricao'},
 		            {data: 'bairro.descricao'},
 		            {data: 'usuario'}, 			    		         
 		            {orderable: false, data: 'data', render:
 		                function( data ) {
 		                    return moment(data).format('DD/MM/YYYY');
 		                }
 		            },
 		            {orderable: false, 
 		             data: 'id',
 		                "render": function(id) {
	                	return '<a class="btn-tabela  btn-tabela-view" href="/solicitacoes/visualizar/'+ 
 		                   		id +'" role="button"><i class="fas fa-eye"></i></a>';
 		                }
 		            }
 		        ]
 		    });
 		}
 		
 		function carregaTabelaPorBairro(id, color, status){
 			var tableSolicitacaoBairro = $('.table-solicitacoes-bairros').DataTable({
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
 		    	 "dom": 'frtp',
 		    	searching: true,
 		 
 		    	lengthMenu: [20],			    	
 		        serverSide: true,
 		       drawCallback: function () {
	 		  		  $('[data-toggle="popover-hover"]').popover({
	 					      html: true,
	 					      trigger: 'hover',
	 					      placement: 'right' 					   
	 				  })
	 		  	    },	 	    		      
 		        ajax: {
 		            url: '/solicitacoes/datatables/server/bairro/' + id,
 		            data: function(d){
 		            	d.status = status
 		            }
 		           
 		        },
 		        
 		        "columnDefs":[
 		        	{"width":"10%", "targets": [4]},
 		        	{
 	 		            targets: [0, 1, 2],
 	 		            render: $.fn.dataTable.render.ellipsis( 10 )
 	 		          }
 		        ],
 		        columns: [
 		        	{data: 'cliente.nome'},
 		            {data: 'assunto.descricao'},		        
 		            {data: 'usuario'}, 			    		         
 		            {orderable: false, data: 'data', render:
 		                function( data ) {
 		                    return moment(data).format('DD/MM/YYYY');
 		                }
 		            },
 		            {orderable: false, 
 		             data: 'id',
 		                "render": function(id) {
 		                	return '<a class="btn-tabela  btn-tabela-view" href="/solicitacoes/visualizar/'+ 
 		                   		id +'" role="button"><i class="fas fa-eye"></i></a>';
 		                }
 		            }
 		        ]
 		    });
 		}
 		function carregaTabelaGroup(id, status){
 			var tableSolicitacao = $('.table-resultados').DataTable({
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
 		    	 "dom": 'frtp',
 		    	searching: true,
 		    	 drawCallback: function () {
 	 		  		  $('[data-toggle="popover-hover"]').popover({
 	 					      html: true,
 	 					      trigger: 'hover',
 	 					      placement: 'right'
 	 					      
 	 					   
 	 				  })
 	 		  	    },	 		
 		    	lengthMenu: [20],			    	
 		        serverSide: true,
 		       		    		      
 		        ajax: {
 		            url: '/solicitacoes/datatables/server/' + id,
 		            data: function(d){
 		            	d.status = status
 		            }
 		           
 		        },
 		       "columnDefs":[      	
 		        	{"width":"1%", "targets": [0]}, 
 		        	{
 	 		            targets: [0, 1, 2],
 	 		            render: $.fn.dataTable.render.ellipsis( 10 )
 	 		          }
 		        
 		        ],
 		        columns: [
 		        	
 		            {data: [0]},
 		            {data: [1]},
 		            {data: [2]}, 			    		         
 		            {orderable: false, data: [3], render:
 		                function( data ) {
 		                    return moment(data).format('DD/MM/YYYY');
 		                }
 		            },
 		            {data: [4]},
 		            {data: [5], render: function(data){
 		            	if(data == true){
 		            		return "Sim";
 		            	}else{
 		            		return "Não";
 		            	}
 		            }
 		            },
 		            {orderable: false, 
 		             data: [6],
 		                "render": function(data, type, row) { 		                
 		                	 return  '<a class="btn-tabela  btn-tabela-view" href="/solicitacoes/visualizar/'+ 
 		                	row[6] +'" role="button"><i class="fas fa-eye"></i></a>';
 		                }
 		            }
 		        ]
 		    });
 		}
 		function carregaTabelaPorBairroESolucao(id, status){
 			var tableSolicitacaoBairro = $('.table-solicitacoes-bairros').DataTable({
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
 		    	 "dom": 'frtp',
 		    	searching: true,
 		 
 		    	lengthMenu: [20],			    	
 		        serverSide: true,
 		       drawCallback: function () {
	 		  		  $('[data-toggle="popover-hover"]').popover({
	 					      html: true,
	 					      trigger: 'hover',
	 					      placement: 'right' 					   
	 				  })
	 		  	    },	 	    		      
 		        ajax: {
 		            url: '/solicitacoes/datatables/server/bairro/' + id,
 		            data: function(d){
 		            	d.status = status
 		            }
 		           
 		        },
 		        
 		        "columnDefs":[
 		        	{"width":"10%", "targets": [4]},
 		        	{
 	 		            targets: [0, 1, 2],
 	 		            render: $.fn.dataTable.render.ellipsis( 10 )
 	 		          }
 		        ],
 		        columns: [
 		        	{data: [0]},
 		            {data: [1]},		        
 		            {data: [2]}, 			    		         
 		            {orderable: false, data: [3], render:
 		                function( data ) {
 		                    return moment(data).format('DD/MM/YYYY');
 		                }
 		            },
 		            {data: [4]},
 		            {data: [5], render: function(data){
 		            	return data == true ? "Sim" : "Não";
 		            }},
 		            {orderable: false, 
 		             data: [6],
 		                "render": function(data, type, row) {
 		                	return  '<a class="btn-tabela  btn-tabela-view" href="/solicitacoes/visualizar/'+ 
 		                	row[6] +'" role="button"><i class="fas fa-eye"></i></a>';
 		                }
 		            }
 		        ]
 		    });
 		}