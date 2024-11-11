<template>
    Export excel
    <button @click="exportExcel">Click</button>
</template>

<script>
import writeXlsxFile from 'write-excel-file';
import Neutralino from '@neutralinojs/lib';
import { Buffer } from 'buffer';

export default {
    name: 'ExportExcel',
    props: ['database'],
    data() {
        return {
            headerBackground: "#4287f5",
            headerColor: "#ffffff",
            dialog: false
        }
    },
    computed: {
    },
    methods: {
        async exportExcel() {
            if(!this.dialog && this.database) {                
                let clienti = Object.keys(this.database.tagli);
                let sheetsName = [];
                let sheets = [];

                clienti.forEach(async (client_key) => {
                    sheetsName.push(client_key);

                    let rows = [];

                    let date = Object.keys(this.database.tagli[client_key]);

                    date.forEach(date_key => {
                        rows.push([
                            { type: String, value: date_key, backgroundColor: this.headerBackground, color: this.headerColor }
                        ]);

                        rows.push([
                            { type: String, value: "Taglio", backgroundColor: this.headerBackground, color: this.headerColor },
                            { type: String, value: "Lunghezza", backgroundColor: this.headerBackground, color: this.headerColor },
                            { type: String, value: "Larghezza", backgroundColor: this.headerBackground, color: this.headerColor },
                            { type: String, value: "Spessore", backgroundColor: this.headerBackground, color: this.headerColor },
                            { type: String, value: "Minuti", backgroundColor: this.headerBackground, color: this.headerColor },
                            { type: String, value: "Diametro tr.", backgroundColor: this.headerBackground, color: this.headerColor },
                            { type: String, value: "Lunghezza tr.", backgroundColor: this.headerBackground, color: this.headerColor },
                            { type: String, value: "Volume tr.", backgroundColor: this.headerBackground, color: this.headerColor },
                            { type: String, value: "Ratio", backgroundColor: this.headerBackground, color: this.headerColor }
                        ]);

                        let volumeTotale = this.database.tagli[client_key][date_key].volume;
                        let metriTotali = this.database.tagli[client_key][date_key].metri;
                        let oreTotali = this.database.tagli[client_key][date_key].ore;
                        let tronchi = Object.keys(this.database.tagli[client_key][date_key].tronchi);

                        tronchi.forEach(tronchi_key => {
                            let diametroTronco = this.database.tagli[client_key][date_key].tronchi[tronchi_key].diametro;
                            let lunghezzaTronco = this.database.tagli[client_key][date_key].tronchi[tronchi_key].lunghezza;
                            let volumeTronco = this.database.tagli[client_key][date_key].tronchi[tronchi_key].volume;
                            let ratio = this.database.tagli[client_key][date_key].tronchi[tronchi_key].ratio;
                            let tagli = this.database.tagli[client_key][date_key].tronchi[tronchi_key].tagli;

                            tagli.forEach((taglio, i) => {
                                let data = [
                                    { type: String, value: (i+1).toString() },
                                    { type: String, value: taglio.lunghezza.toString() },
                                    { type: String, value: taglio.larghezza.toString() },
                                    { type: String, value: taglio.spessore.toString() },
                                    { type: String, value: parseFloat(taglio.durata / 60).toFixed(2).toString() }
                                ];

                                if(i == 0) {
                                    data.push({ type: String, value: diametroTronco.toString() });
                                    data.push({ type: String, value: lunghezzaTronco.toString() });
                                    data.push({ type: String, value: volumeTronco.toString() });
                                    data.push({ type: String, value: ratio.toString() });
                                }

                                rows.push(data);
                            });

                            rows.push([]);
                        });

                        rows.push([
                            { type: String, value: "Volume totale", backgroundColor: this.headerBackground, color: this.headerColor },
                            { type: String, value: "Ore totali", backgroundColor: this.headerBackground, color: this.headerColor },
                            { type: String, value: "Metri totali", backgroundColor: this.headerBackground, color: this.headerColor }
                        ]);

                        rows.push([
                            { type: String, value: volumeTotale.toString() },
                            { type: String, value: oreTotali.toString() },
                            { type: String, value: metriTotali.toString() }
                        ]);

                        rows.push([]);
                    });

                    sheets.push(rows);
                });

                const blob = await writeXlsxFile(sheets, {
                    sheets: sheetsName
                });
                let blobBuffer = await blob.arrayBuffer();
                const buffer = Buffer.from(blobBuffer, 'binary');

                this.dialog = true;

                let path = await Neutralino.os.showSaveDialog("Scarica excel");

                this.dialog = false;

                if(path != "") {
                    if(path.indexOf('.xlsx') === -1) {
                        path += ".xlsx";
                    }

                    await Neutralino.filesystem.writeBinaryFile(path, buffer);
                }
            }
        }
    },
    mounted() {
    }
}
</script>

<style scoped>
</style>