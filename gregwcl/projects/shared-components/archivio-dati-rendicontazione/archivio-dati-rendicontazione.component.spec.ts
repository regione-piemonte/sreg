import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArchivioDatiRendicontazioneComponent } from './archivio-dati-rendicontazione.component';

describe('ArchivioDatiRendicontazioneComponent', () => {
  let component: ArchivioDatiRendicontazioneComponent;
  let fixture: ComponentFixture<ArchivioDatiRendicontazioneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArchivioDatiRendicontazioneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArchivioDatiRendicontazioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
