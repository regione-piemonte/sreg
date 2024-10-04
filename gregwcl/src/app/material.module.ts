import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  MatFormFieldModule, 
  MatInputModule, 
  MatTableModule, 
  MatIconModule, 
  MatToolbarModule, 
  MatMenuModule, 
  MatButtonModule, 
  MatPaginatorModule,
  MatSortModule,
  MatPaginatorIntl,
  MatAutocompleteModule,
  MatSnackBarModule,
  MatStepperModule,
  MatTabsModule,
  MatDatepickerModule,
  MatSelectModule,
  MatNativeDateModule
 } from '@angular/material';
import { StylePaginatorDirective } from './style-paginator.directive';

// Funzioni per definire e rinominare le label del MatPaginator
const rangeLabel = (page: number, pageSize: number, length: number) => {
  if (length == 0 || pageSize == 0) { return `0 di ${length}`; }
  
  length = Math.max(length, 0);

  const startIndex = page * pageSize;

  // If the start index exceeds the list length, do not try and fix the end index to the end.
  const endIndex = startIndex < length ?
      Math.min(startIndex + pageSize, length) :
      startIndex + pageSize;

  return `${startIndex + 1} - ${endIndex} di ${length}`;
}

export function  getItalianPaginatorIntl(){

  const paginatorIntl = new MatPaginatorIntl();
  
  paginatorIntl.firstPageLabel = 'Vai Prima Pagina';
  paginatorIntl.itemsPerPageLabel = 'Elementi per Pagina:';
  paginatorIntl.lastPageLabel = 'Vai Ultima Pagina';
  paginatorIntl.nextPageLabel = 'Vai Pagina Successiva';
  paginatorIntl.previousPageLabel = 'Vai Pagina Precedente';
  paginatorIntl.getRangeLabel = rangeLabel;
  
  return paginatorIntl;

}

@NgModule({
  declarations: [
    StylePaginatorDirective
  ],
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatSelectModule,
    MatIconModule,
    MatToolbarModule,
    MatMenuModule,
    MatButtonModule,
    MatPaginatorModule,
    MatSortModule,
    MatAutocompleteModule,
    MatDatepickerModule,
    MatNativeDateModule, 
    // MatSnackBarModule
    MatStepperModule,
    MatTabsModule,
  ],
  exports: [
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatSelectModule,
    MatIconModule,
    MatToolbarModule,
    MatMenuModule,
    MatButtonModule,
    MatPaginatorModule,
    MatSortModule,
    MatAutocompleteModule,
    MatDatepickerModule,
    MatNativeDateModule, 
    // MatSnackBarModule,
    StylePaginatorDirective,
    MatStepperModule,
    MatTabsModule
  ],
  providers: [
    { provide: MatPaginatorIntl, useValue: getItalianPaginatorIntl() }
  ]
})
export class MaterialModule { }
