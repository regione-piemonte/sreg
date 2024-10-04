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
import { StatoEnteGreg } from '@greg-app/app/dto/StatoEnteGreg';
import { NumberSymbol } from '@angular/common';

@Component({
	selector: 'app-stato-ente-archivio',
	templateUrl: './stato-ente-archivio.component.html',
	styleUrls: ['./stato-ente-archivio.component.css'],
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
export class StatoEnteArchivioComponent implements OnInit {

	@Input() idScheda: number;

	listaStatoEnte: MatTableDataSource<StatoEnteGreg[]>;
	columnsCronologia: Array<string> = [];
	visibileSection: boolean;
	espansa: boolean;

	navigation: Navigation;
	state: string = 'default';
	cronologia: CronologiaGreg = new CronologiaGreg();

	//@Input() public lunghezzalista: number;
	//@Input() public cronologia: CronologiaGreg = new CronologiaGreg();
	//@Output() cronologiain = new EventEmitter<CronologiaGreg>();

	constructor(private router: Router, private route: ActivatedRoute,
		public client: GregBOClient, public toastService: AppToastService) {
	}

	ngOnInit() {

		this.espansa = false;
		if (this.client.azioni.get("CronologiaRegionale")[1]) {
			this.columnsCronologia = ['dal', 'al', 'operatore', 'stato', 'motivazione', 'notaEnte', 'notaInterna'];
			this.visibileSection = true;
		}
		else if (this.client.azioni.get("CronologiaEnte")[1]) {
			this.columnsCronologia = ['dal', 'al', 'operatore', 'stato', 'motivazione', 'nota'];
			this.visibileSection = false;
		}
		this.listaStatoEnte = new MatTableDataSource<StatoEnteGreg[]>();
		// this.client.spinEmitter.emit(true);  NON DEVE PARTIRE QUI ALTRIMENTI INTERFERISCE CON L'EMIT DEI COMPONENTI PADRE
		forkJoin({
			cronologia: this.client.getCronologiaStato(this.idScheda)
		}).subscribe(({ cronologia }) => {
			this.listaStatoEnte.data = cronologia as any;
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