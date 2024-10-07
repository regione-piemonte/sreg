/*
 * Copyright Regione Piemonte - 2024
 * SPDX-License-Identifier: EUPL-1.2
 */

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelezioneProfiloApplicativoComponent } from './selezione-profilo-applicativo.component';

describe('SelezioneProfiloApplicativoComponent', () => {
  let component: SelezioneProfiloApplicativoComponent;
  let fixture: ComponentFixture<SelezioneProfiloApplicativoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelezioneProfiloApplicativoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelezioneProfiloApplicativoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
