#!/bin/bash

echo "Script para limpar os aquivos criados na execução do programa (utilizado principalmente para fazer o commit no github)."
echo "Todos os arquivos (.arff, .dat, .txt) das pastas <dat-files>, <EAL-SelectionClassification/arff-files-iterations>, "
echo "     <EAL-Sort/arff-files-sorted>, <EAL-Splitter/arff-files> e <txt-files> serão apagados, sem chance de desfazer."

echo "Caso deseje continuar, digite <yes>."
read apagar

case "$apagar" in "yes" )
    rm dat-files/*
    rm EAL-SelectionClassification/arff-files-iterations/*
    rm EAL-Sort/arff-files-sorted/*
    rm EAL-Splitter/arff-files/*
    rm txt-files/*
;;

* )
    echo "Você não digitou <yes>."
;;
esac

echo "Tchau!"
exit
