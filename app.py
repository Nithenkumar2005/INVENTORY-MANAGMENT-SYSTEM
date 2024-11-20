from flask import Flask, jsonify, request, send_from_directory
import subprocess

app = Flask(__name__)

# Serve frontend files
@app.route('/')
def home():
    return send_from_directory('frontend', 'index.html')

@app.route('/<path:path>')
def static_files(path):
    return send_from_directory('frontend', path)

# API Endpoints
@app.route('/add-item', methods=['POST'])
def add_item():
    # Data from frontend
    data = request.json
    name = data.get('name')
    quantity = data.get('quantity')
    price = data.get('price')
    
    # Call Java backend
    result = subprocess.run(
        ['java', '-cp', './backend:mysql-connector-java-8.0.x.jar', 'InventoryBackend', 'add', name, str(quantity), str(price)],
        stdout=subprocess.PIPE,
        text=True
    )
    
    return jsonify({'message': result.stdout.strip()})

@app.route('/view-items', methods=['GET'])
def view_items():
    # Call Java backend
    result = subprocess.run(
        ['java', '-cp', './backend:mysql-connector-java-8.0.x.jar', 'InventoryBackend', 'view'],
        stdout=subprocess.PIPE,
        text=True
    )
    
    return jsonify({'items': result.stdout.strip()})

@app.route('/update-item', methods=['PUT'])
def update_item():
    # Data from frontend
    data = request.json
    item_id = data.get('id')
    name = data.get('name')
    quantity = data.get('quantity')
    price = data.get('price')
    
    # Call Java backend
    result = subprocess.run(
        ['java', '-cp', './backend:mysql-connector-java-8.0.x.jar', 'InventoryBackend', 'update', str(item_id), name, str(quantity), str(price)],
        stdout=subprocess.PIPE,
        text=True
    )
    
    return jsonify({'message': result.stdout.strip()})

@app.route('/delete-item', methods=['DELETE'])
def delete_item():
    # Data from frontend
    data = request.json
    item_id = data.get('id')
    
    # Call Java backend
    result = subprocess.run(
        ['java', '-cp', './backend:mysql-connector-java-8.0.x.jar', 'InventoryBackend', 'delete', str(item_id)],
        stdout=subprocess.PIPE,
        text=True
    )
    
    return jsonify({'message': result.stdout.strip()})

if __name__ == '__main__':
    app.run(debug=True)
