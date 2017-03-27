package crudAutomoveis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CRUDAutomoveis {

	private static Connection connection = null;
	static PreparedStatement pstmt;

	public static void conectaBanco() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			System.err.print("ClassNotFoundException: ");
			System.err.println(e.getMessage());
		}
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:bd3");

		} catch (SQLException e) {
			System.err.print("SQLException: ");
			System.err.println(e.getMessage());
		}
	}

	public static String lerEscritaConsole(String message) {
		BufferedReader buf = null;
		try {
			System.out.println("Digite 'CANCELAR' para sair da operacao");
			System.out.print(message);
			buf = new BufferedReader(new InputStreamReader(System.in));
			if (buf.readLine().equals("CANCELAR")) {
				menu();
			}
			return buf.readLine();
		} catch (IOException e) {
			System.out.println("Erro ao ler do console");
			return null;
		}
	}

	public static void menu() {
		do {
			System.out.println("Escolha uma opção:");
			System.out.println(
					"[(1) Consultar a lista de automóveis | (2) Consultar dados de um veículo | (3) Cadastrar novo veículo]");
			System.out.println(
					"[(4) Atualizar preço de tabela | (5) Remover veículo | (6) Execução de comandos SQL | (7) Sair do programa]");
			try {
				int opcao = Integer.parseInt(lerEscritaConsole("Digite o numero de uma das opções acima"));
				switch (opcao) {
				case 1:
					System.out.println("[(1) Consultar a lista de automóveis]");
					consultaListaAutomoveis();
					break;
				case 2:
					System.out.println("[(2) Consultar dados de um veículo]");
					consultaCodigoAutomovel();
					break;
				case 3:
					System.out.println("[(3) Cadastrar novo veículo]");
					inserirAutomovel();
					break;
				case 4:
					System.out.println("[(4) Atualizar preço de tabela]");
					atualizarAutomovel();
					break;
				case 5:
					System.out.println("[(5) Remover veículo]");
					removerAutomovel();
					break;
				case 6:
					System.out.println("[(6) Execução de comandos SQL]");
					executarSQL();
					break;
				default:
					System.out.println("[(7) Sair do programa]");
					System.out.println("Encerrando programa...");
					System.exit(0);
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Digite um numero valido.");
				menu();
			}
		} while (true);
	}

	public static void consultaListaAutomoveis() {
		/*
		 * 1) Consultar a lista de automóveis – A partir dessa opção, o programa
		 * deve exibir listagem com as seguintes informações de todos os
		 * automóveis existentes na base de dados: fabricante, modelo, ano e
		 * preço de tabela;
		 */
		try {
			pstmt = connection.prepareStatement("SELECT * FROM Automoveis");

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int codigo = rs.getInt("codigo");
				int ano = rs.getInt("ano");
				String fabricante = rs.getString("fabricante");
				String modelo = rs.getString("modelo");
				float preco = rs.getFloat("preco_tabela");
				String pais = rs.getString("pais");
				System.out.println(" | " + codigo + " | " + ano + " | " + fabricante + " | " + modelo + " | " + preco
						+ " | " + pais + " | ");
			}

			pstmt.close();
		} catch (SQLException e) {
			System.out.println("erro de consulta");
			menu();
		}
	}

	public static void consultaCodigoAutomovel() {
		/*
		 * 2) Consultar dados de um veículo – os dados de fabricante, modelo,
		 * ano e preço de tabela de um determinado veículo (código de veículo
		 * fornecido pelo usuário) devem ser apresentados.
		 */

		int codigo = 0;

		do {
			String consoleRead = lerEscritaConsole("Digite o codigo do automovel:");
			try {
				codigo = Integer.parseInt(consoleRead);
				try {
					pstmt = connection.prepareStatement(
							"SELECT fabricante, modelo, ano, pais, preco_tabela FROM Automoveis WHERE codigo = ?");
					pstmt.setInt(1, codigo);
					ResultSet rs = pstmt.executeQuery();
					while (rs.next()) {
						int ano = rs.getInt("ano");
						String fabricante = rs.getString("fabricante");
						String modelo = rs.getString("modelo");
						float preco = rs.getFloat("preco_tabela");
						String pais = rs.getString("pais");
						System.out.println(" | " + fabricante + " | " + modelo + " | " + ano + " | " + preco + " | "
								+ pais + " | ");
					}
					pstmt.close();
				} catch (SQLException e) {
					System.out.println("Codigo não reconhecido");
					menu();
				}

			} catch (NumberFormatException e) {
				System.out.println("Digite um numero valido.");
				menu();
			}
		} while (codigo != 0);
	}

	public static void inserirAutomovel() {
		/*
		 * 3) Cadastrar novo veículo – As informações de código, fabricante,
		 * modelo, ano, preço de tabela e país devem ser solicitadas ao usuário
		 * e um novo veículo deve ser cadastrado com base nos dados fornecidos;
		 */
		int codigo;
		int ano;
		String fabricante;
		String modelo;
		float preco_tabela;
		String pais;
		try {
			codigo = Integer.parseInt(lerEscritaConsole("Digite o codigo do novo automovel:"));
			ano = Integer.parseInt(lerEscritaConsole("Digite o ano do novo automovel:"));
			fabricante = lerEscritaConsole("Digite o fabricante do novo automovel:");
			modelo = lerEscritaConsole("Digite o modelo do novo automovel:");
			preco_tabela = Float.parseFloat(lerEscritaConsole("Digite o preço de tabela do novo automovel:"));
			pais = lerEscritaConsole("Digite o pais do novo automovel:");
			try {
				pstmt = connection.prepareStatement(
						"INSERT INTO Automoveis (codigo,ano,fabricante,modelo,preco_tabela,pais) VALUES(?,?,?,?,?,?)");
				pstmt.setInt(1, codigo);
				pstmt.setInt(2, ano);
				pstmt.setString(3, fabricante);
				pstmt.setString(4, modelo);
				pstmt.setFloat(5, preco_tabela);
				pstmt.setString(6, pais);
				pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				System.out.println("Erro ao inserir");
				menu();
			}
		} catch (NumberFormatException e) {
			System.out.println("Digite um numero valido.");
			menu();
		}
	}

	public static void atualizarAutomovel() {
		/*
		 * 4) Atualizar preço de tabela - o preço de tabela de um veículo
		 * (informado pelo usuário) deve ser atualizado para um novo valor
		 * (também fornecido pelo usuário);
		 */
		int codigo;
		float preco_tabela;
		try {
			codigo = Integer.parseInt(lerEscritaConsole("Digite o codigo do automovel a ser atualizado:"));
			preco_tabela = Float.parseFloat(lerEscritaConsole("Digite o novo preco de tabela do automovel:"));
			try {
				pstmt = connection.prepareStatement("UPDATE Automoveis SET preco_tabela = ?  WHERE codigo = ?");
				pstmt.setFloat(1, preco_tabela);
				pstmt.setInt(2, codigo);
				pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				System.out.println("Erro ao atualizar automovel, talvez o codigo seja inexistente");
				menu();
			}
		} catch (NumberFormatException e) {
			System.out.println("Digite um numero valido.");
			menu();
		}

	}

	public static void removerAutomovel() {
		/*
		 * 5) Remover veículo - um veículo deve ser excluído da base de dados
		 * com base em um código fornecido pelo usuário. Deve ser solicitada
		 * confirmação da exclusão;
		 */
		int codigo;

		try {
			codigo = Integer.parseInt(lerEscritaConsole("Digite o codigo do automovel a ser removido:"));
			try {
				pstmt = connection.prepareStatement("DELETE FROM Automoveis WHERE codigo = ? ");
				pstmt.setInt(1, codigo);
				pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException e) {
				System.out.println("Erro ao remover automovel, talvez o codigo seja inexistente");
				menu();
			}
		} catch (NumberFormatException e) {
			System.out.println("Digite um numero valido.");
			menu();
		}
	}

	public static void executarSQL() {
		/*
		 * 6) Execução de comandos SQL - O usuário poderá especificar um comando
		 * SQL de forma iterativa e o programa deverá executá-lo e apresentar
		 * seu resultado;
		 */

		String query = lerEscritaConsole(
				"Digite o comando SQL que será executado [SELECT|INSERT|DELETE|UPDATE]");
		try {
			pstmt = connection.prepareStatement(query);
			pstmt.execute();
			System.out.println("O comando SQL '"+query+"' foi executado");
			
			ResultSet rs = pstmt.getResultSet();
			while (rs.next()) {
				int c = rs.getMetaData().getColumnCount();
				String result = "";
				for(int i = 0; i<c;i++)
					result.concat(" | "+rs.getString(i));
				System.out.println(result+" | ");
			}
			
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("Erro ao executar query");
			menu();
		}
	}

	public static void main(String args[]) {
		conectaBanco();
		menu();
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
