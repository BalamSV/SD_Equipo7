/*
ANTONIO SEGURA DAVID 
BRAVO AMESCUA EMILIANO 
SÁNCHEZ VALDIVIA BALAM 
GRUPO: 2TV7
 */

#include "suma.h"

int *
suma_1_svc(dupla_int *argp, struct svc_req *rqstp)
{
	static int  result;

	printf("El procedimiento ha sido invocado remotamente\n"); //Paso 4
	printf("Server is working \n"); //Paso 11
	printf("parameters: %d, %d \n",argp->a, argp->b); //Paso 11
	result = argp->a + argp->b; //Paso 13
	printf("returning: %d\n",result); //Paso 13

	return &result; //Paso 11
}
