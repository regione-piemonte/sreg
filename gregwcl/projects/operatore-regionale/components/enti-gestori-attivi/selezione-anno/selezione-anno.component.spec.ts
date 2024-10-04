import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SelezioneAnnoComponent } from './selezione-anno.component';

describe('SelezioneAnnoComponent', () => {
  let component: SelezioneAnnoComponent;
  let fixture: ComponentFixture<SelezioneAnnoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SelezioneAnnoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelezioneAnnoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
