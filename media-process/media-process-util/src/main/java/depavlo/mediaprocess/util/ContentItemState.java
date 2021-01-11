package depavlo.mediaprocess.util;

/**
 * The Enum ContentItemState that represent Content Item state.
 * 
 * @author Pavlo Degtyaryev
 */
public enum ContentItemState {

	/** The processing waiting state. */
	WAITING,
	/** Tprocessed and ready for coding. */
	CODED,
	/** processed and ready for coding. */
	COPIED,
	/** processed and has children to be copied. */
	HASCODED,
	/** processed and has children to be coded. */
	HASCOPIED;

}
