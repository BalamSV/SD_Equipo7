/*
ANTONIO SEGURA DAVID 
BRAVO AMESCUA EMILIANO 
SÁNCHEZ VALDIVIA BALAM 
GRUPO: 2TV7
 */

#include "suma.h"
#include <stdio.h> //Paso 16


void
suma_prog_1(char *host, int a, int b) //Paso 15
{
	CLIENT *clnt;
	int  *result_1;
	dupla_int  suma_1_arg;

#ifndef	DEBUG
	clnt = clnt_create (host, SUMA_PROG, SUMA_VERS, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror (host);
		exit (1);
	}
#endif	/* DEBUG */

suma_1_arg.a = a; //Paso 9
suma_1_arg.b = b; //Paso 9
result_1 = suma_1(&suma_1_arg, clnt); //Paso 9


	result_1 = suma_1(&suma_1_arg, clnt);
	if (result_1 == (int *) NULL) { //Paso 14
		clnt_perror (clnt, "call failed"); //Paso 14
	}

else { //Paso 14
printf("result = %d\n",*result_1); //Paso 14
}
#ifndef	DEBUG
	clnt_destroy (clnt);
#endif	
}



int
main (int argc, char *argv[]) //Paso 17, editar main
{
	char *host;
	int a,b; //Se declaran las variables

	if (argc != 4) { //Se verifica la cantidad de argumentos
		printf ("usage: %s server_host num1 num2\n", argv[0]); //Si no son 4, se manda un mensaje de como se debe de usar el programa
		exit (1);
	}
	host = argv[1]; //se le almacena el nombre del servidor
	
	if((a = atoi(argv[2])) == 0 && *argv[2] != '0'){ // se convierte en un entero y se guarda en a y se verifica la convercion
	fprintf(stderr, "invalid value: %s\n",argv[2]); //mensaje de error
	exit(1); //sale del programa
	}

	if((b = atoi(argv[3])) == 0 && *argv[3] != '0'){ // se convierte en un entero y se guarda en a y se verifica la convercion
	fprintf(stderr, "invalid value: %s\n",argv[3]); //mensaje de error
	exit(1); //sale del programa
	}

	suma_prog_1 (host, a, b); //se llama a la funcion con sus respectivos argumentos
exit (0);
}
