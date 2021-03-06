package com.alex.classloader;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.LLOAD;
import org.apache.bcel.generic.LSTORE;
import org.apache.bcel.generic.LSUB;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

public class ClassTransfor {
	
	public static byte[] Tranform(String _clazz){
		try {
//			System.out.println("Tranform方法开始..............");
//			System.out.println("Repository的加载器: " + Repository.class.getClassLoader());
			
			JavaClass clazz = Repository.lookupClass(_clazz);
			ClassGen classGen = new ClassGen(clazz);
			
			
			// 获得 Class的常量池
			ConstantPoolGen cPoolGen = classGen.getConstantPool();

			// 1.常量池中添加System.out.println
			// 添加java.io.PrintStream
			int printStreamClass = cPoolGen.addClass("java.io.PrintStream");
			// 添加java.lang.System
			int systemClass = cPoolGen.addClass("java.lang.System");
			// 添加成员变量out
			int outField = cPoolGen.addFieldref("java.lang.System", "out", "Ljava/io/PrintStream;");
			// 添加println方法
			int printlnMethodS = cPoolGen.addMethodref("java.io.PrintStream", "println", "(Ljava/lang/String;)V");
			int printlnMethod = cPoolGen.addMethodref("java.io.PrintStream", "println", "(J)V");

			// 2.常量池中添加System.nanoTime()
			int nanoTimeMethod = cPoolGen.addMethodref("java.lang.System", "nanoTime", "()J");

			// 遍历所有方法，给每一个方法植入性能计数器
			Method[] allMethods = classGen.getMethods();
			Method sourceMethod = null;
			for (int i = 0; i < allMethods.length; i++) {
				sourceMethod = allMethods[i];
				
				//调试信息
//				System.out.println(sourceMethod.getName());
				
				String methodName = sourceMethod.getName();
				if (!"<init>".equals(methodName) && !"main".equals(methodName) && !"test".equals(methodName)) {
					
					// 构造我们所希望的方法
					MethodGen methodGen = new MethodGen(sourceMethod, clazz.getClassName(), cPoolGen);
					methodGen.setMaxStack(255);
					// 获取操作指令集
					InstructionList instructionList = methodGen.getInstructionList();
					InstructionHandle[] handles = instructionList.getInstructionHandles();

					// 添加Local变量用来存储nanoTime调用产生的返回值
					LocalVariableGen startTime = methodGen.addLocalVariable("startTime", Type.LONG, null, null);
					// 添加新操作，在代码第一行插入新的代码,该代码调用System.nanoTime()
					InstructionHandle newHandle = instructionList.insert(handles[0], new INVOKESTATIC(nanoTimeMethod));
					InstructionHandle storeStartTime = instructionList.append(newHandle, new LSTORE(startTime.getIndex()));

					// 在代码尾部插入新代码统计时间
					LocalVariableGen endTime = methodGen.addLocalVariable("endTime", Type.LONG, null, null);
					InstructionHandle endHandle = instructionList.insert(handles[handles.length-2],
							new INVOKESTATIC(nanoTimeMethod));
					InstructionHandle storeEndTime = instructionList.append(endHandle, new LSTORE(endTime.getIndex()));

					// 计算差值
					LocalVariableGen durationTime = methodGen.addLocalVariable("durationTime", Type.LONG, null, null);
					InstructionHandle loadEndTime = instructionList.append(storeEndTime, new LLOAD(endTime.getIndex()));
					InstructionHandle loadStartTime = instructionList.append(loadEndTime, new LLOAD(startTime.getIndex()));
					InstructionHandle caculateDuration = instructionList.append(loadStartTime, new LSUB());
					InstructionHandle storeDurationTime = instructionList.append(caculateDuration,
							new LSTORE(durationTime.getIndex()));

					// 输出
					//获得方法名
					int nameIndex = cPoolGen.addString(clazz.getClassName() + " " + methodName + " 耗时(纳秒)：");
					InstructionHandle outHandle1 = instructionList.append(storeDurationTime, new GETSTATIC(outField));
					InstructionHandle msgHandle1 = instructionList.append(outHandle1, new LDC(nameIndex));
					InstructionHandle printHandle1 = instructionList.append(msgHandle1, new INVOKEVIRTUAL(printlnMethodS));
					
					InstructionHandle outHandle = instructionList.append(printHandle1, new GETSTATIC(outField));
					InstructionHandle msgHandle = instructionList.append(outHandle, new LLOAD(durationTime.getIndex()));
					InstructionHandle printHandle = instructionList.append(msgHandle, new INVOKEVIRTUAL(printlnMethod));
					// 更新
					classGen.replaceMethod(sourceMethod, methodGen.getMethod());
				}

			}
		
			JavaClass target = classGen.getJavaClass();
			byte[] bytes = target.getBytes();
//			System.out.println("Tranform方法结束..............");
			return bytes;
//			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
