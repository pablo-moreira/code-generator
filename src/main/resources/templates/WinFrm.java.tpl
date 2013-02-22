package ${pacoteWinFrm};

import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;

import br.com.atosdamidia.comuns.controlador.componente.WinFrmCrud;
import br.com.atosdamidia.comuns.visao.interceptors.annotations.TrataErro;
import ${pacoteDAO}.${EntidadeNome}DAO;
import ${pacoteEntidade}.${EntidadeNome};
import ${pacoteManager}.${EntidadeNome}Manager;
${winFrmEntidadeImports}

@Named
@ConversationScoped
@TrataErro
public class WinFrm${EntidadeNome} extends WinFrmCrud<${EntidadeNome},${entidadeIdClass}> {

	private static final long serialVersionUID = 1L;
	
	${winFrmEntidadeAtributos}
	public WinFrm${EntidadeNome}() {
		super(${EntidadeNome}.class, ${EntidadeNome}DAO.class, ${EntidadeNome}Manager.class, "${ArtigoDefinido} ${entidadeRotulo} foi salv${artigoDefinido} com sucesso!", "${ArtigoDefinido} ${entidadeRotulo} foi excluíd${artigoDefinido} com sucesso!");
	}
	
	${winFrmEntidadeMetodos}
}