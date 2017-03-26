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

	public static void ConectaBanco() {
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

	public static void ConsultaListaAutomoveis() {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void ConsultaCodigoAutomovel() {
		int codigo = 0;

		try {
			System.out.println("Digite 0 (zero) para cancelar consulta");
			System.out.print("Digite o codigo do automovel:");
			BufferedReader buf = new BufferedReader(new InputStreamReader(System.in));
			try {
				int i = Integer.parseInt(buf.readLine());
				if (i == 0) {
					System.out.println("Consulta Cancelada");
					System.exit(0);
				}
				codigo = i;

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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (NumberFormatException e) {
				System.out.println("Digite um numero valido.");
			}
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	public static void main(String args[]) {
		ConectaBanco();

		ConsultaListaAutomoveis();
		ConsultaCodigoAutomovel();

		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
