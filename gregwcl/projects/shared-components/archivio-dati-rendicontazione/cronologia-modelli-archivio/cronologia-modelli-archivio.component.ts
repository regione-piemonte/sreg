import { Component, EventEmitter, HostListener, Input, OnInit, Output } from '@angular/core';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { MatTableDataSource } from '@angular/material/table';
import { CronologiaGreg } from '@greg-app/app/dto/CronologiaGreg';
import { GregBOClient } from '@greg-app/app/GregBOClient';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { AppToastService } from '@greg-shared/toast/app-toast.service';
import { forkJoin } from 'rxjs';
import { RendicontazioneEnteGreg } from '@greg-app/app/dto/RendicontazioneEnteGreg';
import { DatiEnteGreg } from '@greg-app/app/dto/DatiEnteGreg';

@Component({
	selector: 'app-cronologia-modelli-archivio',
	templateUrl: './cronologia-modelli-archivio.component.html',
	styleUrls: ['./cronologia-modelli-archivio.component.css'],
	animations: [
		// Each unique animation requires its own trigger. The first argument of the trigger function is the name
		trigger('rotatedState', [
			state('rotated', style({ transform: 'rotate(0)' })),
			state('default', style({ transform: 'rotate(-180deg)' })),
			transition('rotated => default', animate('100ms ease-out')),
			transition('default => rotated', animate('100ms ease-in'))
		])
	]
})
export class CronologiaModelliArchivioComponent implements OnInit {

	listaCronologiaAssociata: MatTableDataSource<CronologiaGreg[]>;
	columnsCronologia: Array<string> = [];
	visibileSection: boolean;
	espansa: boolean;

	navigation: Navigation;
	state: string = 'default';
	cronologia: CronologiaGreg = new CronologiaGreg();
	somePlaceholder: string;
	rendicontazione: RendicontazioneEnteGreg;
	datiEnte: DatiEnteGreg;
	//@Input() public lunghezzalista: number;
	//@Input() public cronologia: CronologiaGreg = new CronologiaGreg();
	//@Output() cronologiain = new EventEmitter<CronologiaGreg>();

	constructor(private router: Router, private route: ActivatedRoute,
		public client: GregBOClient, public toastService: AppToastService) {
		this.navigation = this.router.getCurrentNavigation();
		let enteValues: string[] = [];
		this.route.fragment.subscribe((frag: string) => {
			enteValues.push(frag);
		});
		this.rendicontazione = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.rendicontazione : enteValues[0][0];
		this.datiEnte = this.navigation.extras && this.navigation.extras.state ? this.navigation.extras.state.datiEnte : enteValues[0][1];
	}

	ngOnInit() {

		this.espansa = false;
		if (this.client.azioni.get("CronologiaRegionale")[1]) {
			this.columnsCronologia = ['dataEora', 'utente', 'modello', 'statoRendicontazione', 'notaEnte', 'notaInterna'];
			this.visibileSection = true;
			this.somePlaceholder = "Inserisci qui la nota per l'Ente";
		}
		else if (this.client.azioni.get("CronologiaEnte")[1]) {
			this.columnsCronologia = ['dataEora', 'utente', 'modello', 'statoRendicontazione', 'nota'];
			this.visibileSection = false;
			this.somePlaceholder = "Inserisci qui la nota";
		}
		this.listaCronologiaAssociata = new MatTableDataSource<CronologiaGreg[]>();
		// this.client.spinEmitter.emit(true);  NON DEVE PARTIRE QUI ALTRIMENTI INTERFERISCE CON L'EMIT DEI COMPONENTI PADRE
		forkJoin({
			cronologia: this.client.getCronologia(this.rendicontazione.idRendicontazioneEnte)
		}).subscribe(({ cronologia }) => {
			this.listaCronologiaAssociata.data = cronologia as any;
			// this.client.spinEmitter.emit(false);
		},
			err => {
				// this.client.spinEmitter.emit(false);
			});
	}

	@HostListener('document:updateCronoEmitter')
	updateCrono() {
		this.ngOnInit();
		this.espansa = false;
		this.state = 'rotated';
		this.apricronologia();
		this.cronologia.notaEnte = null;
		this.cronologia.notaInterna = null;
	}

	apricronologia() {
		if (this.espansa)
			this.espansa = false;
		else
			this.espansa = true;
		this.state = (this.state === 'default' ? 'rotated' : 'default');
	}

	updateCronologia() {
		this.client.cronologia = this.cronologia;
	}


}