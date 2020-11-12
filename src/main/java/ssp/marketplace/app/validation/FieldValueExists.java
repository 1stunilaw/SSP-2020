package ssp.marketplace.app.validation;

public interface FieldValueExists {
    /**
     * Проверяет существует ли данное значение для данного поля
     *
     * @param value Значение для проверки
     * @param fieldName Название поля, по которому будет проверяться существование значения
     * @return True если значение в данном поле существует, иначе false
     * @throws UnsupportedOperationException
     */
    boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException;
}
