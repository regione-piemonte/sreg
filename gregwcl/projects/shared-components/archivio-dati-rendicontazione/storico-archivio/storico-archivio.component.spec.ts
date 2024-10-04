import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { StoricoArchivioComponent } from '@greg-shared/archivio-dati-rendicontazione/storico-archivio/storico-archivio.component';


describe('StoricoArchivioComponent', () => {
  let component: StoricoArchivioComponent;
  let fixture: ComponentFixture<StoricoArchivioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StoricoArchivioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StoricoArchivioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
