<template>
    <div class="colonna main">
        <h3>Lista lame</h3>
        <p>Imposta dei filtri per data oppure esporta tutto</p>

        <br>

        <div class="filter">
            <div class="header">
                <div>Filtro attivo:</div>
                <button :class="['btn', filter ? 'btn-primary' : '']" @click="filter = !filter">{{ filter ? 'Sì' : 'No' }}</button>
            </div>

            <div id="picker" :class="filter ? '' : 'disabled'">
                <div>da</div>
                <input type="text" name="start">
                <div>a</div>
                <input type="text" name="end">
            </div>
        </div>

        <br>

        <button class="btn btn-primary" @click="exportExcel">Esporta excel</button>
    </div>
</template>

<script>
import writeXlsxFile from 'write-excel-file';
import { Buffer } from 'buffer';
import 'vanillajs-datepicker/css/datepicker-bulma.css';
import { DateRangePicker } from 'vanillajs-datepicker';

export default {
    name: 'ExportExcel',
    props: ['database'],
    data() {
        return {
            headerBackground: "#4287f5",
            headerColor: "#ffffff",
            dialog: false,
            filter: false
        }
    },
    computed: {
    },
    methods: {
        async exportExcel() {
            const rangepicker = document.getElementById('picker').rangepicker;
            let pickers = rangepicker.datepickers;

            let startDate = null;
            let endDate = null;

            if(pickers[0].dates.length > 0) {
                startDate = new Date(pickers[0].dates[0]);
                console.error('SOCO', startDate);
            }

            if(pickers[1].dates.length > 0) {
                endDate = new Date(pickers[1].dates[0]);
            }

            if(!this.dialog && this.database) {                
                let clienti = Object.keys(this.database.tagli);

                let sheetsName = ['Lame'];
                
                let lame_rows = [[
                    { type: String, value: "Nome", backgroundColor: this.headerBackground, color: this.headerColor },
                    { type: String, value: "Ore max.", backgroundColor: this.headerBackground, color: this.headerColor },
                    { type: String, value: "Metri max.", backgroundColor: this.headerBackground, color: this.headerColor },
                    { type: String, value: "Ore", backgroundColor: this.headerBackground, color: this.headerColor },
                    { type: String, value: "Metri", backgroundColor: this.headerBackground, color: this.headerColor },
                    { type: String, value: "Ultima aff.", backgroundColor: this.headerBackground, color: this.headerColor }
                ]];
                
                this.database.lame.forEach(lama => {
                    let lr = [
                        { type: String, value: lama.name },
                        { type: String, value: parseFloat(lama.maxOre).toFixed(2) },
                        { type: String, value: parseFloat(lama.maxMetri).toFixed(2) },
                        { type: String, value: parseFloat(lama.ore).toFixed(2) },
                        { type: String, value: parseFloat(lama.metri).toFixed(2) },
                        { type: String, value: lama.ultimaAffilatura ?  lama.ultimaAffilatura : '-' }
                    ];

                    lame_rows.push(lr);
                });

                let sheets = [lame_rows];

                clienti.forEach(async (client_key) => {
                    let rows = [];
                    let date = Object.keys(this.database.tagli[client_key]);

                    let filteredDates = date.filter(d => {
                        let pts = d.split("/");
                        let dd = new Date(pts[2] + "-" + pts[1] + "-" + pts[0]);

                        let condit = true;

                        if(this.filter) {
                            if(startDate != null && endDate != null) condit = dd >= startDate && dd <= endDate;
                            if(startDate != null && endDate == null) condit = dd >= startDate;
                            if(startDate == null && endDate != null) condit = dd <= endDate;
                        }

                        console.error('SOCO', startDate, endDate);

                        return condit;
                    });

                    filteredDates.forEach(date_key => {
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

                    console.error('SOCO', rows);

                    sheets.push(rows);
                    sheetsName.push(client_key);
                });

                let now = new Date();
                let fileName = "Report_segheria_" + String(now.getDate()).padStart(2, '0') + "/" + String(now.getMonth()+1).padStart(2, '0') + "/" + now.getFullYear();

                let blob = await writeXlsxFile(sheets, {
                    sheets: sheetsName
                })

                this.saveFile(blob, fileName);
            }
        },
        saveFile(blob, filename) {
            if (window.navigator.msSaveOrOpenBlob) {
                window.navigator.msSaveOrOpenBlob(blob, filename);
            } else {
                const a = document.createElement('a');
                document.body.appendChild(a);
                const url = window.URL.createObjectURL(blob);
                a.href = url;
                a.download = filename;
                a.click();
                setTimeout(() => {
                    window.URL.revokeObjectURL(url);
                    document.body.removeChild(a);
                }, 0)
            }
        }
    },
    mounted() {
        const elem = document.getElementById('picker');
        const rangepicker = new DateRangePicker(elem, {
            // ...options
        }); 
    }
}
</script>

<style scoped>
h3 {
    font-size: 16px;
    color: #0088cc;
    font-weight: 500;
    margin-top: 0;
    line-height: 14px;
    margin-bottom: 10px;
}

.colonna.main {
    padding: 14px;
}

.datepicker-input {
    height: 30px;
    margin-bottom: 0;
}

#picker {
    display: flex;
    align-items: center;
}

#picker div {
    margin: 0 10px;
}

.filter {
    display: flex;
    flex-direction: column;
}

.filter > .header {
    display: flex;
    align-items: center;
    margin-bottom: 14px;
}

.filter > .header> div {
    margin-right: 10px;
}

button {
    outline: none !important;
}

#picker.disabled {
    opacity: 0.5;
    pointer-events: none;
}
</style>