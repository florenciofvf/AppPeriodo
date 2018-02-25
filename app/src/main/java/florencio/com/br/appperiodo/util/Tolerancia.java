package florencio.com.br.appperiodo.util;

public class Tolerancia {
	private final long menosHorario;
	private final long maisHorario;

	Tolerancia(long menosHorario, long maisHorario) {
		this.menosHorario = menosHorario;
		this.maisHorario = maisHorario;
	}

	public long getMenosHorario() {
		return menosHorario;
	}

	public long getMaisHorario() {
		return maisHorario;
	}
}