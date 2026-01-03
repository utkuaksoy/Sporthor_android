//
//  CKDatePicker.swift
//  ComponentKit
//
//  Created by derTurke on 17.07.2025.
//

import UIKit
import DesignKit

public enum CKPickerType {
    case year
    case month
    case date
    case time
    case dateAndTime
}

public protocol CKDatePickerDelegate: AnyObject {
    func selectedYear(_ year: String, textField: CKTextField)
    func selectedMonth(_ month: String, textField: CKTextField)
    func selectedDate(_ date: Date, textField: CKTextField)
    func selectedTime(_ time: Date, textField: CKTextField)
}

public extension CKDatePickerDelegate {
    func selectedYear(_ year: String, textField: CKTextField) {}
    func selectedMonth(_ month: String, textField: CKTextField) {}
    func selectedDate(_ date: Date, textField: CKTextField) {}
    func selectedTime(_ time: Date, textField: CKTextField) {}
}

public final class CKDatePicker: CKTextField {
    // MARK: - Properties

    public var pickerType: CKPickerType = .year {
        didSet {
            configurePicker()
        }
    }

    private lazy var pickerView: UIPickerView = {
        let pickerView = UIPickerView()
        pickerView.delegate = self
        pickerView.dataSource = self
        return pickerView
    }()

    private lazy var datePicker: UIDatePicker = {
        let datePicker = UIDatePicker()
        datePicker.addTarget(self, action: #selector(handleDatePicker(_:)), for: .valueChanged)
        return datePicker
    }()

    private var years: [Int] {
        let currentYear = Calendar.current.component(.year, from: Date())
        return Array(1800...currentYear)
    }

    private var months: [String] {
        let formatter = DateFormatter()
        return formatter.monthSymbols
    }
    
    // MARK: - Members
    public weak var ckDatePickerDelegate: CKDatePickerDelegate?

    // MARK: - Picker Setup

    private func configurePicker() {
        switch pickerType {
        case .year, .month:
            self.inputView = pickerView
            pickerView.reloadAllComponents()
        case .date:
            datePicker.datePickerMode = .date
            self.inputView = datePicker
        case .time:
            datePicker.datePickerMode = .time
            self.inputView = datePicker
        case .dateAndTime:
            datePicker.datePickerMode = .dateAndTime
            self.inputView = datePicker
        }
    }

    @objc private func handleDatePicker(_ sender: UIDatePicker) {
        switch pickerType {
        case .date, .dateAndTime:
            self.text = formatDate(sender.date)
            ckDatePickerDelegate?.selectedDate(sender.date, textField: self)
        case .time:
            self.text = formatTime(sender.date)
            ckDatePickerDelegate?.selectedTime(sender.date, textField: self)
        default:
            break
        }
    }

    private func formatDate(_ date: Date) -> String {
        let formatter = DateFormatter()
        formatter.dateStyle = .medium
        return formatter.string(from: date)
    }

    private func formatTime(_ date: Date) -> String {
        let formatter = DateFormatter()
        formatter.timeStyle = .short
        return formatter.string(from: date)
    }
}

// MARK: - UIPickerViewDelegate & DataSource
extension CKDatePicker: UIPickerViewDelegate, UIPickerViewDataSource {
    public func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }

    public func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        switch pickerType {
        case .year:
            return years.count
        case .month:
            return months.count
        default:
            return 0
        }
    }

    public func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        switch pickerType {
        case .year:
            return "\(years[row])"
        case .month:
            return months[row]
        default:
            return nil
        }
    }

    public func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        switch pickerType {
        case .year:
            let year = years[row]
            self.text = "\(year)"
            ckDatePickerDelegate?.selectedYear("\(year)", textField: self)
        case .month:
            let month = months[row]
            self.text = month
            ckDatePickerDelegate?.selectedMonth(month, textField: self)
        default:
            break
        }
    }
}

extension CKDatePicker {
    override public func canPerformAction(_ action: Selector, withSender sender: Any?) -> Bool {
        return false
    }
    
    override public func caretRect(for position: UITextPosition) -> CGRect {
        return .zero
    }
    
    override public func selectionRects(for range: UITextRange) -> [UITextSelectionRect] {
        return []
    }
}
