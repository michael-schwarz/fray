package org.pastalab.fray.instrumentation.base.visitors

import org.objectweb.asm.*
import org.objectweb.asm.Opcodes.ASM9

object LINEINFO {
  var lyne = -1
  var klassName = ""
}

open class LineClassVisitor(cv: ClassVisitor) : ClassVisitor(ASM9,cv) {

  override fun visit(
    version: Int,
    access: Int,
    name: String?,
    signature: String?,
    superName: String?,
    interfaces: Array<out String>?
  ) {
    if(name != null)
      LINEINFO.klassName = name
    super.visit(version, access, name, signature, superName, interfaces)
  }

  override fun visitMethod(
    access: Int,
    name: String,
    descriptor: String,
    signature: String?,
    exceptions: Array<out String>?
  ): MethodVisitor {
    val mv = super.visitMethod(access, name, descriptor, signature, exceptions)

    return object : MethodVisitor(Opcodes.ASM9,mv) {
      override fun visitLineNumber(lineNo: Int, start: Label) {
        LINEINFO.lyne = lineNo
        super.visitLineNumber(lineNo, start)
      }
    }
  }
}
