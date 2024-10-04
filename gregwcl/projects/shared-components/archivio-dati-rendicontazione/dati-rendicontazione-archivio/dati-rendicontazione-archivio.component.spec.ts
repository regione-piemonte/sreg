import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DatiRendicontazioneArchivioComponent } from './dati-rendicontazione-archivio.component';

describe('DatiRendicontazioneArchivioComponent', () => {
  let component: DatiRendicontazioneArchivioComponent;
  let fixture: ComponentFixture<DatiRendicontazioneArchivioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DatiRendicontazioneArchivioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DatiRendicontazioneArchivioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
