import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { ListaGreg } from '@greg-app/app/dto/ListaGreg';
import { ProfiloGreg } from '@greg-app/app/dto/ProfiloGreg';
import { UserInfoGreg } from '@greg-app/app/dto/UserInfoGreg';
import { GregBOClient } from '@greg-app/app/GregBOClient';

@Component({
  selector: 'app-selezione-profilo-applicativo',
  templateUrl: './selezione-profilo-applicativo.component.html',
  styleUrls: ['./selezione-profilo-applicativo.component.css']
})
export class SelezioneProfiloApplicativoComponent implements OnInit {

 navigation: Navigation;
 utente : UserInfoGreg;
 profiloSelezionato: ProfiloGreg;
 listaSelezionata: ListaGreg;
 utenteSelezionato : UserInfoGreg;
 profilosel: ProfiloGreg[]=[];
 listasel: ListaGreg[]=[];

 abilitaconferma: boolean = false;

  constructor(private router: Router, private route: ActivatedRoute, 
    public client: GregBOClient) {
   this.navigation = this.router.getCurrentNavigation();
   this.utente = this.navigation.extras? this.navigation.extras.state.utente : null;
 }

  ngOnInit() {
	this.client.spinEmitter.emit(true);
	this.profiloSelezionato = new ProfiloGreg("","","","","",null,null);
	if (this.utente.listaprofili.length==1)
		this.profiloSelezionato = this.utente.listaprofili[0];
	this.client.spinEmitter.emit(false);
  }

 onClick() {

	 this.listasel.push(this.listaSelezionata);
     this.profiloSelezionato.listaenti = this.listasel;
	 this.profilosel.push(this.profiloSelezionato);
	 this.utenteSelezionato = new UserInfoGreg(this.client.utenteloggato.codFisc,this.client.utenteloggato.cognome,this.client.utenteloggato.nome,'',this.profilosel);	 
	 this.client.verificaprofilo(this.utenteSelezionato);
	}
	
	onSelectionProfiloChanged() {
		this.listaSelezionata = new ListaGreg('','',null);
		this.abilitaconferma = false;
		if (this.profiloSelezionato.listaenti.length==1){
		this.listaSelezionata = this.profiloSelezionato.listaenti[0];
		this.client.listaSelezionata = this.listaSelezionata;
		this.abilitaconferma = true;
		}
	  }

	onSelectionListaChanged() {
		this.abilitaconferma = true;
		this.client.profiloSelezionato = this.profiloSelezionato;
		this.client.listaSelezionata = this.listaSelezionata;
	}

}
