# Java Spreadsheet

## About

### Purpose
This program is a simplified spreadsheet program. Its purpose is to track words, numbers, and formulas. When specified with an "=", the program will attempt to calculate the given formula. All sheets made in this program are saved with a .sprd extension. You can open multiple windows of this program. Other features include saving, opening, and closing a spreadsheet file. 

### Author
Aaron Bellis

## How To Use
### File:
#### New: 
Opens a new (blank) spreadsheet.

Hotkey: Ctrl + N

#### Save:  
Opens the file explorer for the user to save the current spreadsheet in the preferred location.

Hotkey: Ctrl + S

#### Open:  
Opens the file explorer for the user to save the current spreadsheet in the preferred location.

Hotkey: Ctrl + O

#### Close:  
Closes the current spreadsheet window.

Hotkey: Ctrl + Q

### Help:
#### About:

Text document that provides information about the purpose, author and updates on this program.

#### How to Use:

Text document that provides information on features about this program.

### Cell Properties:
#### Current Cell:

Non-editable text box that informs the user which cell is selected.

#### Value:

Non-editable text box that informs the user of the selected cell's value. The evaluated value of a Formula, words and numbers, will be displayed here.

#### Contents:

Editable text box that informs the user about the contents of the selected cell. This is where the user can type and edit information about the cell. When the contents of the text box begins with an '=' then the cell is calculated as a formula. The results are displayed in the value box.

#### Spreadsheet/ Grid:

A table of cells that tracks the inputs (contents) from the user. Each cell has a name. The names begin with a letter between A-Z and ends with a number between 1-99.

### Formulas: 

#### About:

Formulas are written in standard infix notation using standard precedence rules. The program will attempt to interpret any text entered into the ‘Contents’ text field that begin with the character ‘=’ as a formula.

#### Allowed symbols:

The four types of allowed symbols in a formula are:

1.  Non-negative numbers written using double-precision floating-point syntax
    
    0, 2, 3.14, 6E256
    
2.  Variables that consist of a letter followed by a number between 1-99
    
	   A3, D9, B14, Z99
    
3.  Parentheses
    
	   (, )
    
4.  The four operator symbols
    
	   +, -, *, /

#### Limitations:

Formulas are not capable of handling negative numbers or performing implied multiplication

✅  =(0 - 3)*(2 + 8)  
❌  =-3(2 + 8)