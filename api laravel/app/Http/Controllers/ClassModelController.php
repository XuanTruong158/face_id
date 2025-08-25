<?php

namespace App\Http\Controllers;

use App\Models\ClassModel;
use Illuminate\Http\Request;

class ClassModelController extends Controller
{
    /**
     * Display a listing of the resource.
     */
    public function index()
    {
        $lophocphan = ClassModel::with(['course', 'lecture'] )->get();

        return response()->json($lophocphan);
    }

    public function store(Request $request)
    {
        $classModel = ClassModel::create($request->all());
        return response()->json($classModel);
    }

    /**
     * Display the specified resource.
     */
    public function show($id)
    {
        
        return response()->json(ClassModel::find($id));
    }

    /**
     * Update the specified resource in storage.
     */
    public function update(Request $request, $id)
    {
        $classModel = ClassModel::find($id);
        $classModel->update($request->all());
        return response()->json($classModel);
       //
    }

    /**
     * Remove the specified resource from storage.
     */
    public function destroy($id)

    {
        $classModel = ClassModel::find($id);
        $classModel->delete();
        return response()->json(['message' => 'ClassModel deleted']);
    }
}
