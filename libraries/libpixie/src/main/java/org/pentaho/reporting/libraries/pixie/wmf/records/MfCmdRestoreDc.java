/*
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
* Foundation.
*
* You should have received a copy of the GNU Lesser General Public License along with this
* program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
* or from the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
* See the GNU Lesser General Public License for more details.
*
* Copyright (c) 2001 - 2013 Object Refinery Ltd, Hitachi Vantara and Contributors..  All rights reserved.
*/

package org.pentaho.reporting.libraries.pixie.wmf.records;

import org.pentaho.reporting.libraries.pixie.wmf.MfRecord;
import org.pentaho.reporting.libraries.pixie.wmf.MfType;
import org.pentaho.reporting.libraries.pixie.wmf.WmfFile;

/**
 * The RestoreDC function restores a device context (DC) to the specified state. The DC is restored by popping state
 * information off a stack created by earlier calls to the SaveDC function.
 * <p/>
 * <code> BOOL RestoreDC( HDC hdc,       // handle to DC int nSavedDC   // restore state ); </code>
 * <p/>
 * Parameters nSavedDC [in] Specifies the saved state to be restored. If this parameter is positive, nSavedDC represents
 * a specific instance of the state to be restored. If this parameter is negative, nSavedDC represents an instance
 * relative to the current state. For example, 0x01 restores the most recently saved state.
 */
public class MfCmdRestoreDc extends MfCmd {
  private int dcId;

  public MfCmdRestoreDc() {
  }

  /**
   * Replays the command on the given WmfFile.
   *
   * @param file the meta file.
   */
  public void replay( final WmfFile file ) {
    if ( dcId == 0 ) {
      return;
    }

    if ( dcId > 0 ) {
      file.restoreDCState( dcId );
    } else {
      file.restoreDCState( file.getStateCount() - dcId );
    }
  }

  /**
   * Creates a empty unintialized copy of this command implementation.
   *
   * @return a new instance of the command.
   */
  public MfCmd getInstance() {
    return new MfCmdRestoreDc();
  }

  /**
   * Reads the command data from the given record and adjusts the internal parameters according to the data parsed.
   * <p/>
   * After the raw record was read from the datasource, the record is parsed by the concrete implementation.
   *
   * @param record the raw data that makes up the record.
   */
  public void setRecord( final MfRecord record ) {
    final int id = record.getParam( 0 );
    setNSavedDC( id );
  }

  /**
   * Creates a new record based on the data stored in the MfCommand.
   *
   * @return the created record.
   */
  public MfRecord getRecord()
    throws RecordCreationException {
    final MfRecord record = new MfRecord( 1 );
    record.setParam( 0, getNSavedDC() );
    return record;
  }

  /**
   * Reads the function identifier. Every record type is identified by a function number corresponding to one of the
   * Windows GDI functions used.
   *
   * @return the function identifier.
   */
  public int getFunction() {
    return MfType.RESTORE_DC;
  }

  public int getNSavedDC() {
    return dcId;
  }

  public void setNSavedDC( final int id ) {
    this.dcId = id;
  }

  public String toString() {
    final StringBuffer b = new StringBuffer();
    b.append( "[RESTORE_DC] nSavedDC=" );
    b.append( getNSavedDC() );
    return b.toString();
  }

  /**
   * A callback function to inform the object, that the x scale has changed and the internal coordinate values have to
   * be adjusted.
   */
  protected void scaleXChanged() {
  }

  /**
   * A callback function to inform the object, that the y scale has changed and the internal coordinate values have to
   * be adjusted.
   */
  protected void scaleYChanged() {
  }
}
