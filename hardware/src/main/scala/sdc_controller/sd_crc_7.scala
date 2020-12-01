package sdc_controller

import chisel3._
import chisel3.util._

class sd_crc_7 extends Module {
  val io = IO(new Bundle {
    val bitval = Input(UInt(1.W))
    val enable = Input(UInt(1.W))
    val bitstrb = Input(UInt(1.W))
    val clear = Input(UInt(1.W))
    val crc = Output(UInt(7.W))
  })

  val crc_reg = Reg(Bits(7.W))
  val inv = io.bitval ^ crc_reg(6)

  when((RegNext(io.bitstrb) =/= io.bitstrb) || (RegNext(io.clear) =/= io.clear)) {
    when(io.clear === 1.U){
      crc_reg := 0.U
    }.elsewhen(io.enable === 1.U){
      val crc_reg_2 = crc_reg(2)
      crc_reg := crc_reg << 1
      crc_reg(3) := crc_reg_2 ^ inv
      crc_reg(0) := inv
    }
  }
  io.crc <> crc_reg
}
