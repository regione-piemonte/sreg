import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { DettaglioPrestazioneArchivioComponent } from './dettaglio-prestazione-archivio.component';


describe('DettaglioPrestazioneArchivioComponent', () => {
  let component: DettaglioPrestazioneArchivioComponent;
  let fixture: ComponentFixture<DettaglioPrestazioneArchivioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DettaglioPrestazioneArchivioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DettaglioPrestazioneArchivioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
