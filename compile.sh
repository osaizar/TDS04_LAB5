echo "Compiling everything..."
make target
make clobber
make idl
make c
make s
echo "Done!"
echo "Running ORBD"
make orbd

