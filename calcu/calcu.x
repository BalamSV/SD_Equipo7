struct dupla_int {
	int a;
	int b;
	char c;
};

program CALCU_PROG{
	version CALCU_VERS{
		int CALCU(dupla_int)=1;
	} = 1;
} = 0x23451111;
