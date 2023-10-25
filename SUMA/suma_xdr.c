/*
ANTONIO SEGURA DAVID 
BRAVO AMESCUA EMILIANO 
SÁNCHEZ VALDIVIA BALAM 
GRUPO: 2TV7
No modificar este archivo
 */

#include "suma.h"

bool_t
xdr_dupla_int (XDR *xdrs, dupla_int *objp)
{
	register int32_t *buf;

	 if (!xdr_int (xdrs, &objp->a))
		 return FALSE;
	 if (!xdr_int (xdrs, &objp->b))
		 return FALSE;
	return TRUE;
}
