import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { DettaglioPrestazioneComponent } from './dettaglio-prestazione.component';


describe('DettaglioPrestazioneComponent', () => {
  let component: DettaglioPrestazioneComponent;
  let fixture: ComponentFixture<DettaglioPrestazioneComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DettaglioPrestazioneComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DettaglioPrestazioneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
