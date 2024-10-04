import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PulsantiFunzioniComponent } from './pulsanti-funzioni.component';

describe('PulsantiFunzioniComponent', () => {
  let component: PulsantiFunzioniComponent;
  let fixture: ComponentFixture<PulsantiFunzioniComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PulsantiFunzioniComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PulsantiFunzioniComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
