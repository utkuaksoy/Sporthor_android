//
//  CKSelectionTextField.swift
//  ComponentKit
//
//  Created by derTurke on 16.07.2025.
//

import UIKit
import DesignKit

public final class CKSelectionTextField: UIView {
    // MARK: - UI Elements
    private lazy var textField: UITextField = {
        let textField = PaddedTextField()
        textField.delegate = self
        textField.textColor = ColorName.contentStrong900.color
        textField.backgroundColor = ColorName.backgroundWeak100.color
        textField.setBorderWidth(1)
        textField.setBorderColor(.clear)
        textField.setCornerRadius(8)
        textField.font = .body04Compact
        textField.textPadding = UIEdgeInsets(top: 12, left: 16, bottom: 12, right: 16)
        textField.translatesAutoresizingMaskIntoConstraints = false
        return textField
    }()
    
    private lazy var tableView: UITableView = {
        let tableView = UITableView()
        tableView.dataSource = self
        tableView.delegate = self
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.backgroundColor = ColorName.backgroundWeak100.color
        tableView.separatorColor = ColorName.borderSub300.color
        tableView.separatorInset = UIEdgeInsets(top: 0, left: 16, bottom: 0, right: 16)
        tableView.isHidden = true
        tableView.register(UITableViewCell.self, forCellReuseIdentifier: "Cell")
        return tableView
    }()
    
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(cornerRadius: 8)
        stackView.addArrangedSubviews([textField, tableView])
        stackView.clipsToBounds = true
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()

    // MARK: - Members
    private var delegate: CKSelectionTextFieldDelegate?
    private var items: [String] = []
    private var filteredItems: [String] = []
    
    // MARK: - Initializers
    public init(delegate: CKSelectionTextFieldDelegate? = nil,
                items: [String] = [],
                text: String = "",
                placeholder: String = "") {
        super.init(frame: .zero)
        setupView()
        bind(delegate: delegate, items: items, text: text, placeholder: placeholder)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    private func setupView() {
        backgroundColor = .clear
        addSubview(stackView)
        
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: topAnchor),
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            textField.heightAnchor.constraint(equalToConstant: 48),
            tableView.heightAnchor.constraint(equalToConstant: 100),
        ])
    }
    
    // MARK: - Custom Methods
    public func bind(delegate: CKSelectionTextFieldDelegate? = nil,
              items: [String] = [],
              text: String = "",
              placeholder: String = "") {
        self.delegate = delegate
        self.items = items
        self.textField.text = text
        updatePlaceholder(placeholder: placeholder)
    }
    
    public func updatePlaceholder(placeholder: String = "", color: UIColor = ColorName.contentSoft600.color, font: UIFont = .body03Compact) {
        if !placeholder.isEmpty {
            let placeholderText = NSAttributedString(
                string: placeholder,
                attributes: [.foregroundColor: color]
            )
            textField.attributedPlaceholder = placeholderText
        }
    }
}

// MARK: - UITableViewDelegate, UITableViewDataSource
extension CKSelectionTextField: UITableViewDelegate, UITableViewDataSource {
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return filteredItems.count
    }

    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath)
        cell.textLabel?.text = filteredItems[indexPath.row]
        cell.textLabel?.font = .body03Compact
        cell.textLabel?.textColor = ColorName.contentStrong900.color
        cell.selectionStyle = .none
        cell.backgroundColor = .clear
        cell.contentView.backgroundColor = .clear
        return cell
    }

    public func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let selectedName = filteredItems[indexPath.row]
        textField.text = selectedName
        tableView.isHidden = true
//        endEditing(true)
    }
}

extension CKSelectionTextField: UITextFieldDelegate {
    public func textFieldDidBeginEditing(_ textField: UITextField) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.textField.layer.borderColor = ColorName.borderStrong900.color.cgColor
        }
        
        delegate?.ckSelectionTextFieldDidBeginEditing(textField.text ?? "")
    }
    
    public func textFieldDidEndEditing(_ textField: UITextField) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.textField.layer.borderColor = UIColor.clear.cgColor
        }
        delegate?.ckSelectionTextFieldDidEndEditing(textField.text ?? "")
    }
    
    public func textFieldDidChangeSelection(_ textField: UITextField) {
        guard let text = textField.text else { return }
        filteredItems = items.filter { $0.lowercased().contains(text.lowercased()) }
        tableView.isHidden = filteredItems.isEmpty
        tableView.reloadData()
        delegate?.ckSelectionTextFieldDidChangeSelection(text)
    }
}
