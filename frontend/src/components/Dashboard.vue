<template>
    <div class="dashboard">
        <div class="row">
            <div class="field-wrapper small-input">
                <label>Tagli</label>
                <div class="input">
                    <input type="text" :value="tagliTotali" class="readonly" readonly>
                </div>
            </div>

            <div class="field-wrapper">
                <label>Metri tagliati</label>
                <div class="input">
                    <input type="text" :value="parseFloat(metriTotali).toFixed(2)" class="readonly" readonly>
                    <small>m</small>
                </div>
            </div>

            <div class="field-wrapper">
                <label>Tempo di taglio</label>
                <div class="input">
                    <input type="text" :value="timeClock" class="readonly" readonly>
                </div>
            </div>

            <div class="field-wrapper">
                <label>Volume</label>
                <div class="input">
                    <input type="text" :value="parseFloat(volume).toFixed(2)" class="readonly" readonly>
                    <small>m<sup>3</sup></small>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="field-wrapper">
                <label>Nome cliente</label>
                <div class="input">
                    <input type="text" v-model="currentTaglio.cliente">
                </div>
            </div>

            <div class="field-wrapper">
                <label>lunghezza</label>
                <div class="input">
                    <input type="text" v-model="currentTaglio.lunghezza" @change="currentTaglio.lunghezza = filterComma(currentTaglio.lunghezza)">
                    <small>cm</small>
                </div>
            </div>

            <div class="field-wrapper">
                <label>Larghezza</label>
                <div class="input">
                    <input type="text" v-model="currentTaglio.larghezza"
                        @change="currentTaglio.larghezza = filterComma(currentTaglio.larghezza)">
                    <small>cm</small>
                </div>
            </div>

            <div class="field-wrapper">
                <label>Spessore</label>
                <div class="input">
                    <input type="text" v-model="currentTaglio.spessore"
                        @change="currentTaglio.spessore = filterComma(currentTaglio.spessore)">
                    <small>cm</small>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="field-wrapper">
                <label>Diametro tronco</label>
                <div class="input">
                    <input type="text" v-model="tronco.diametro"
                        @change="tronco.diametro = filterComma(tronco.diametro)">
                    <small>cm</small>
                </div>
            </div>

            <div class="field-wrapper">
                <label>Lunghezza tronco</label>
                <div class="input">
                    <input type="text" v-model="tronco.lunghezza"
                        @change="tronco.lunghezza = filterComma(tronco.lunghezza)">
                    <small>cm</small>
                </div>
            </div>

            <div class="field-wrapper">
                <label>Volume tronco</label>
                <div class="input">
                    <input type="text" :value="parseFloat(tronco.volume).toFixed(2)" class="readonly" readonly>
                    <small>m<sup>3</sup></small>
                </div>
            </div>

            <div class="field-wrapper">
                <label>Ratio</label>
                <div class="input">
                    <input type="text" :value="parseFloat(ratio).toFixed(2)" class="readonly" readonly>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="field-wrapper">
                <div class="input">
                    <button class="btn btn-primary" @click="azzeraTaglio">Azzera</button>
                </div>
            </div>

            <div class="field-wrapper">
                <div class="input">
                    <button class="btn btn-primary" @click="cambioTronco">Cambio tronco</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'Dashboard',
    props: ['database','socket'],
    data() {
        return {
            tronco: {
                volume: 0,
                diametro: 0,
                lunghezza: 0
            },
            volume: 0,
            tagliTotali: 0,
            metriTotali: 0,
            secondiTotali: 0,
            ratio: 0,
            currentTaglio: {
                cliente: "",
                lunghezza: 0,
                larghezza: 0,
                spessore: 0,
                durata: 0
            }
        }
    },
    computed: {
        timeClock() {
            let sec_num = parseInt(this.secondiTotali, 10);
            let hours = Math.floor(sec_num / 3600);
            let minutes = Math.floor((sec_num - (hours * 3600)) / 60);
            let seconds = sec_num - (hours * 3600) - (minutes * 60);

            if (hours < 10) { hours = "0" + hours; }
            if (minutes < 10) { minutes = "0" + minutes; }
            if (seconds < 10) { seconds = "0" + seconds; }

            return hours + ':' + minutes + ':' + seconds;
        }
    },
    methods: {
        newTaglio(secondi) {
            this.currentTaglio.durata = secondi;
            this.secondiTotali += secondi;

            // metri tagliati in metri
            this.metriTotali += parseFloat(this.currentTaglio.lunghezza) / 100;

            // volum tagliato in metri cubi
            this.volume += (parseFloat(this.currentTaglio.lunghezza) * parseFloat(this.currentTaglio.larghezza) * parseFloat(this.currentTaglio.spessore)) / 1000000;

            // tagli totali
            this.tagliTotali++;

            this.database.tagli.push(this.currentTaglio);
            this.socket.emit('save-db', this.database);

            this.currentTaglio.durata = 0;

            this.ratio = parseFloat(this.volume).toFixed(2) / parseFloat(this.tronco.volume).toFixed(2);

            if(this.database.lama_in_uso > -1) {
                this.database.lame[this.database.lama_in_uso].metri += parseFloat(this.currentTaglio.lunghezza) / 100;
                this.database.lame[this.database.lama_in_uso].ore += secondi / 3600;
                this.database.lame[this.database.lama_in_uso].parziale += parseFloat(this.currentTaglio.lunghezza) / 100;
            }
            
            this.socket.emit('db-sabe', this.database);

            this.$emit('taglio');
        },
        filterNumbers(string) {
            if (string == "") string = "0";
            let firstCommaIndex = string.indexOf('.');
            let result = string.replace(/\D/g, '');
            result = result.replace(/\.(?=(.*\.)*)/g, '');
            if (firstCommaIndex !== -1) {
                result = result.slice(0, firstCommaIndex) + '.' + result.slice(firstCommaIndex);
            }
            return result == "" ? 0 : result
        },
        filterComma(string) {
            if (string.endsWith('.')) {
                return string.replace('.', '');
            }
            return string;
        },
        calcolaVolumeTronco() {
            if (this.tronco.lunghezza > 0 && this.tronco.diametro > 0) {
                // volume tronco
                let raggio = parseFloat(this.tronco.diametro) / 2;
                let superficeCerchio = Math.PI * Math.pow(raggio, 2);
                this.tronco.volume = (superficeCerchio * this.tronco.lunghezza) / 1000000;

                this.ratio = parseFloat(this.volume).toFixed(2) / parseFloat(this.tronco.volume).toFixed(2);
            }
        },
        azzeraTaglio() {
            this.currentTaglio.larghezza = 0;
            this.currentTaglio.spessore = 0;
        },
        cambioTronco() {
            this.tronco.volume = 0;
            this.tronco.diametro = 0;
            this.tronco.lunghezza = 0;
            this.tronco.ratio = 0;
        }
    },
    mounted() {
    },
    watch: {
        'currentTaglio.lunghezza'(nv) {
            this.currentTaglio.lunghezza = this.filterNumbers(nv);
        },
        'currentTaglio.larghezza'(nv) {
            this.currentTaglio.larghezza = this.filterNumbers(nv);
        },
        'currentTaglio.spessore'(nv) {
            this.currentTaglio.spessore = this.filterNumbers(nv);
        },
        'tronco.diametro'(nv) {
            this.tronco.diametro = this.filterNumbers(nv);
            this.calcolaVolumeTronco();
        },
        'tronco.lunghezza'(nv) {
            this.tronco.lunghezza = this.filterNumbers(nv);
            this.calcolaVolumeTronco();
        }
    }
}
</script>

<style scoped>
.dashboard {
    display: flex;
    flex-direction: column;
    padding: 10px 30px;
}

.field-wrapper.small-input {
    max-width: 100px;
}

.field-wrapper {
    position: relative;
    display: flex;
    flex-direction: column;
    max-width: 150px;
    padding: 10px;
}

.field-wrapper>label {
    font-size: 13px;
}

.field-wrapper>.input {
    width: 100%;
    height: 34px;
    display: flex;
    align-items: center;
    border: 1px solid #ccc;
    box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
    border-radius: 4px;
}

.field-wrapper>.input:has(input:focus) {
    border-color: rgba(82, 168, 236, 0.8);
    box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075), 0 0 8px rgba(82, 168, 236, 0.6);
}

.field-wrapper>.input>input {
    background: none;
    border: 0;
    width: 100%;
    text-align: right;
    height: 34px;
    margin-top: 9px;
    box-shadow: none;
    font-size: 14px;
    color: #555;
}

.field-wrapper>.input>small {
    opacity: 0.5;
    margin-right: 8px;
}

input.readonly {
    background-color: white;
    cursor: default;
    pointer-events: none;
}

.field-wrapper > .input:has(button) {
    border: 0;
    box-shadow: none;
}

.row {
    display: flex;
    border-bottom: 1px solid #ebebeb;
}

.row:last-child {
    border-bottom: 0;
}
</style>