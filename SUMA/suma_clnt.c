/*
ANTONIO SEGURA DAVID 
BRAVO AMESCUA EMILIANO 
SÁNCHEZ VALDIVIA BALAM 
GRUPO: 2TV7
No modificar este archivo 
 */

#include <memory.h> /* para memset */
#include "suma.h"


static struct timeval TIMEOUT = { 25, 0 };

int *
suma_1(dupla_int *argp, CLIENT *clnt)
{
	static int clnt_res;

	memset((char *)&clnt_res, 0, sizeof(clnt_res));
	if (clnt_call (clnt, SUMA,
		(xdrproc_t) xdr_dupla_int, (caddr_t) argp,
		(xdrproc_t) xdr_int, (caddr_t) &clnt_res,
		TIMEOUT) != RPC_SUCCESS) {
		return (NULL);
	}
	return (&clnt_res);
}
